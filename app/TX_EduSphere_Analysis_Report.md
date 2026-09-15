# TX EduSphere — Full Project Analysis & Recommendations

## 0. What This Project Actually Is

Before anything else: this is **not** a web app with a separate backend. It's a single **native Android app** (Kotlin + Jetpack Compose, package `com.tx.edusphere`, app name "TX EduSphere") that talks directly to **Supabase** (Postgres + Auth + Realtime as a BaaS) and to the **Google Gemini API** for an AI assistant feature. There is:

- No custom backend/server code of any kind (no Node/Django/Spring/etc.)
- No SQL schema, migrations, or RLS (Row Level Security) policy files in the repo
- No web frontend, no admin web portal — everything is the one Android client
- A committed `.git` history and a full `app/build/` output folder were bundled into the ZIP (should never be shipped/committed — adds ~thousands of stale files and exposes build artifacts)

This matters a lot for the analysis: several things that would normally be "backend" concerns (auth, authorization, validation, business rules) are currently either **missing entirely** or **implemented client-side only**, which is the source of the most serious issues below.

I read the actual source (not just folder names) — DI modules, repositories, view models, navigation graph, the auth flow, the AI assistant, and the app manifest — to ground every point below in real code.

---

## 1. Architecture — What's Implemented

**Stack:** Kotlin 2.2, Jetpack Compose, Material 3, Hilt (DI), Navigation-Compose, DataStore (preferences), Supabase (Postgrest/Auth/Realtime), Ktor client, Kotlinx Serialization, Google Mobile Ads SDK, direct HTTP calls to Gemini.

**Layering:** The project does follow a clean-ish `presentation / domain / data` split as prescribed in its own `Architecture.md`:
- `domain/model` — plain data classes (Student, Faculty, Assignment, Grade, Announcement, Event, Timetable, etc.)
- `domain/repository` — interfaces (`AuthRepository`, `StudentRepository`, `AiAssistantRepository`)
- `data/repository` — implementations, `data/remote/dto` — Supabase DTOs with `toDomain()`/`fromDomain()` mappers
- `presentation/<feature>` — Compose screens + one ViewModel per feature area, using Hilt injection

This part is genuinely reasonable for a solo/small Android project and is more organized than a lot of portfolio projects of this scope. The problems are not in the folder structure — they are in what the implementations actually do.

**Reality check on "backend integration":** `StudentRepositoryImpl` holds **all data as in-memory `MutableStateFlow` collections seeded with hardcoded sample data**. On `init`, it fires a one-time, one-directional background sync (`supabasePostgrest.from(...).select()`) per table and silently swallows any failure (empty `catch` blocks, no logging, no retry, no user-facing error). Every write (`addStudent`, `updateStudent`, `deleteStudent`, etc.) updates the in-memory list immediately and then does a **fire-and-forget** `upsert()` to Supabase in a detached coroutine — if that call fails, the failure is silently discarded and the UI has already shown success. This means:
- Data is **not actually persisted locally** (no Room/SQLite) — a process death/app restart loses anything created since the one-time sync, unless the background write happened to succeed.
- There is **no offline queue**, no conflict resolution, and no real "sync" (it's a single read at startup, not a live subscription — even though Supabase Realtime is a declared dependency and installed in `SupabaseModule`, it is **never actually used anywhere** in the codebase).
- IDs for new entities are assigned client-side in an ad-hoc way (e.g., `"STU" + (100..999).random()` in the AI assistant), which risks collisions with no server-side uniqueness enforcement visible in the client.

**Verdict:** the "repository pattern" exists structurally, but the actual data layer is a **mock/demo data layer with an unreliable, best-effort Supabase side-channel**, not a production data layer.

---

## 2. Security — This Is the Most Important Section

I want to be direct: several of these are **severe, verifiable vulnerabilities** in the code as written, not theoretical concerns. Interestingly, the project's own `rules.md` and `Architecture.md` explicitly forbid every one of them ("never store passwords in plaintext," "no fake functionality," "no hardcoded secrets") — the rules exist, but the implementation violates its own rules in multiple places.

### 2.1 Critical: Authentication has a universal bypass
In `AuthRepositoryImpl.login()`:
```kotlin
} catch (e: Exception) {
    // Graceful fallback for local/demo credentials if remote auth fails or offline
    if (email.isNotBlank() && password.length >= 6) {
        preferenceManager.setLoginState(true, role.name)
        Result.success(Unit)
    } else { ... }
}
```
If the real Supabase sign-in call throws **for any reason** (wrong password, network hiccup, unknown email, Supabase outage), the app falls back to **logging the user in anyway** as long as the password is 6+ characters. The same pattern exists in `register()`. This is a full authentication bypass — anyone can "log in" as any email address with any 6-character string.

### 2.2 Critical: Role is entirely self-selected by the client
On the login screen, the user first picks "Student Portal" or "Admin/Faculty Portal" — this choice is passed straight through to `preferenceManager.setLoginState(true, role.name)` with **no server-side verification that the account actually holds that role**. Combined with 2.1, anyone can grant themselves Admin access with no valid account at all. Downstream, `NavGraph` and every management screen (`StudentListScreen`, `AddEditFacultyScreen`, grade/announcement/event/timetable CRUD, etc.) only use this client-stored role to decide whether to *show* edit/delete buttons — there is no route guard and no repository-level permission check. Any authenticated session (real or bypassed) can navigate directly to any admin route or call any repository write method.

### 2.3 Critical: Passwords are stored in plaintext on-device
```kotlin
val USER_PASSWORD = stringPreferencesKey("user_password")
val userPassword: Flow<String> = dataStore.data.map { it[USER_PASSWORD] ?: "password123" }
suspend fun updatePassword(newPassword: String) { dataStore.edit { it[USER_PASSWORD] = newPassword } }
```
`SecurityScreen`'s "Change Password" flow reads/writes this plaintext value directly (comparing the typed "current password" against it) and **never calls Supabase Auth's password-update API at all**. So: (a) real account passwords are unrecoverable/unchanged through this screen — it's fake functionality operating on a decoy value — and (b) a real secret is stored unencrypted in DataStore, which is not encrypted at rest by default.

### 2.4 High: `allowBackup="true"` with no exclusion rules
`AndroidManifest.xml` sets `android:allowBackup="true"`, and both `backup_rules.xml` and `data_extraction_rules.xml` are the **untouched Android Studio template stubs** (no `<include>`/`<exclude>` rules at all). Combined with #2.3, this means the plaintext password and any session data can be extracted via `adb backup`, cloud backup, or device-to-device transfer.

### 2.5 High: Secrets and keys shipped in the client
- Supabase URL + anon key are **hardcoded directly in `SupabaseModule.kt`** and committed to git (rather than sourced from `local.properties`/`BuildConfig` like the Gemini key is). Supabase anon keys are designed to be public *only if RLS policies are correctly configured on every table* — nothing in this repo (no SQL/policy files) demonstrates that RLS is set up, and the client-side-only permission checks described above strongly suggest it is not.
- The **Gemini API key** is injected via `BuildConfig` and used in `GeminiService` by appending it as a URL query parameter, called **directly from the mobile client**. Any user can extract this key from the compiled APK (`strings`/decompilation — trivially so, since `release { isMinifyEnabled = false }` and `proguard-rules.pro` is empty, meaning **no code shrinking/obfuscation at all** in release builds) and use it for their own billed requests.

### 2.6 High: The AI assistant can be tricked into destructive actions by any non-Student role
`AiAssistantRepositoryImpl` only refuses write actions (create/delete student, event, assignment) when `userRole == STUDENT`; Admin *and* Faculty are both allowed to execute them unconditionally after a "confirmation" step. Given #2.1/#2.2, "Faculty" is a role anyone can grant themselves. Separately, the delete-confirmation payloads are **hardcoded stand-ins, not parsed from the user's message** — e.g. `DELETE_STUDENT` always targets `studentId = "1"` and `DELETE_EVENT` always targets `eventId = "1"` regardless of what the user asked to delete. A teacher typing "delete assignment about the science fair" will, after confirming, delete whatever object actually has ID `"1"` — a real risk of unintended data loss even for a legitimate admin.

### 2.7 Medium: Biometric lock is a decorative toggle
`SecurityScreen` has a "Require Biometrics On Launch" switch that only writes a boolean into DataStore (`securityBiometric`). There is no `BiometricPrompt` integration anywhere in the codebase, and `MainActivity` never checks this flag before rendering the app. Turning it on does nothing.

### 2.8 Low/Medium: Ads SDK in a product likely used by minors
Google Mobile Ads (`play-services-ads`) is initialized unconditionally in `EduSphereApp`, and a `NativeAdCard` component exists in the shared UI kit. A school app used directly by students (who may be minors) needs an explicit, documented policy for ad requests (non-personalized ads for known-under-13 users, COPPA/Google Families Policy compliance, GDPR-K consent flows for the EU, etc.). None of that policy plumbing is present — ads appear to be wired the same way they would be in a generic consumer app.

### 2.9 Dead/leftover attack surface
- `.git/` directory and the entire `app/build/` output (including compiled `.dex` files) were included in the delivered ZIP. If this was ever pushed to a public repo as-is, decompiled class files and git history (which can contain earlier, possibly-more-sensitive versions of the hardcoded keys above) are exposed.
- Test files reference the stale package `com.example.schoolmanagement` (leftover from the original Android Studio template, before the app was renamed to `com.tx.edusphere`) — a sign the project was renamed without a full cleanup pass.

### Security recommendations, in priority order
1. Remove the offline/error fallback in `login()`/`register()` entirely. Authentication should only ever succeed via a verified Supabase (or other IdP) response.
2. Make role a **server-truth** value: store it in the `auth.users` metadata or a `profiles` table with RLS, read it *from* the authenticated session/JWT claims after login, and stop letting the login screen set it. Add role checks in the repository layer (or better, enforce via Postgres RLS policies) — not just conditional UI rendering.
3. Delete the local plaintext password preference entirely. Route "change password" through `supabaseAuth.updateUser(password = ...)` (re-authenticating first), and use Android Keystore-backed encrypted storage (e.g. `EncryptedSharedPreferences`/DataStore with Tink) for anything sensitive that must be cached.
4. Set `android:allowBackup="false"` (or write real `data_extraction_rules.xml` exclusions) until secrets are properly handled.
5. Move all secrets out of source: Supabase anon key can stay client-side **only after RLS is written and audited** for every table; the Gemini key must move behind a small server-side proxy (a single Supabase Edge Function/Cloud Function is enough) so it never ships inside the APK.
6. Enable `isMinifyEnabled = true` + a real `proguard-rules.pro` for release builds.
7. Add a real BiometricPrompt gate tied to the existing toggle, or remove the toggle until it's implemented.
8. Write and test actual RLS policies in Supabase (student can only read/write their own rows; faculty scoped to their classes; admin unrestricted) and add integration tests that assert unauthorized reads/writes are rejected server-side, not just hidden client-side.
9. Add a documented ad-consent/child-safety flow before shipping ads to a student-facing surface, or move ads off any screen students use and keep them (if at all) out of an education product entirely — many school-software buyers will reject a product that shows ads to children on principle.
10. Stop committing `.git` metadata and `build/` output in deliverables; add a proper `.gitignore`.

---

## 3. Bugs & Broken Functionality (verified in code)

| # | Issue | Evidence |
|---|---|---|
| 1 | **"Fees" and "Messages" are dead navigation routes** | `Screen.Fees` and `Screen.Messages` are referenced from `HomeScreen` quick actions and `ExploreScreen` module cards, but **no matching `composable(...)` exists in `NavGraph.kt`**. Tapping either will crash the app with a Navigation-Compose "destination not found" exception. |
| 2 | **"Change Password" doesn't change the real password** | Operates entirely on the plaintext local DataStore value; Supabase Auth's actual password is untouched (see §2.3). |
| 3 | **Biometric toggle does nothing** | No `BiometricPrompt` usage anywhere; flag is never read on launch. |
| 4 | **AI assistant delete actions target hardcoded IDs**, not the entity the user actually named (see §2.6) | `preparePendingAction()` in `AiAssistantRepositoryImpl`. |
| 5 | **`resetPassword(studentId)` is a no-op that always reports success** | `override suspend fun resetPassword(studentId: String): Result<Unit> = Result.success(Unit)` — an admin using this feature is told it worked when nothing happened. |
| 6 | **Remote sync silently fails and is invisible to the user** | Every Supabase call in `StudentRepositoryImpl` is wrapped in `try { } catch (e: Exception) { /* comment only */ }` — no error surfaced, no retry, no offline indicator. |
| 7 | **Realtime dependency installed but unused** | `Realtime` is set up in `SupabaseModule` and provided via Hilt, but nothing subscribes to a channel anywhere — dead dependency adding APK size and attack surface for nothing. |
| 8 | **Leftover home-screen widget provider (`Tx`/`tx.xml`)** | Auto-generated Android Studio App Widget boilerplate, unrelated to school management, left in the project (`Tx.kt`, `layout/tx.xml`, `xml/tx_info.xml`, `drawable-nodpi/example_appwidget_preview.png`). Dead code and app bloat. |
| 9 | **Stale test package** | `ExampleUnitTest`/`ExampleInstrumentedTest` still live under `com.example.schoolmanagement`, the pre-rename template package — confirms these were never actually written for this project. |
| 10 | **No `Parent` role despite the PRD requiring one** | `UserRole` enum only has `STUDENT, ADMIN, FACULTY, NONE`. `PRD.md` explicitly lists Parent as a primary persona with distinct needs (child attendance, fees, communication) — it's entirely absent from the actual model. |

---

## 4. Feature Coverage vs. the Project's Own PRD

The project ships its own `PRD.md`. Grading the implementation against its own stated goals is the fairest way to assess completeness.

| PRD Area | Status | Notes |
|---|---|---|
| Authentication (FR-01) | ⚠️ Present but broken | See §2.1/§2.2 — exists, but not secure or correct |
| Profile view/edit (FR-02) | ✅ Implemented | Local DataStore-backed, functional for basic fields |
| Preferences persistence (FR-03) | ✅ Implemented | Theme/language/notification/privacy toggles persist via DataStore |
| Notifications center (FR-04) | ⚠️ Partial | `NotificationsScreen` has filtering/read-state UI, but the notification list itself is **hardcoded mock data** (`Notification(4, "Fee payment reminder"...)`), not backed by any real event source, push service, or backend table |
| Home academic dashboard (FR-05) | ✅ Implemented (mock-backed) | Present, but numbers come from the in-memory mock repository, not verified live data |
| Explore module discovery (FR-06) | ⚠️ Partial | Search/category UI exists; two of its own listed modules (Fees, Messages) are dead links (bug #1 above) |
| Logout (FR-07) | ✅ Implemented | Clears prefs and back stack correctly |
| Help & support (FR-08) | ✅ Implemented (static) | FAQ/contact/report screens exist but are static content, no ticketing backend |
| Error handling (FR-09) | ⚠️ Inconsistent | Some screens show loading/empty/error; the data layer's silent `catch` blocks undermine this everywhere sync is involved |
| Persistence across restarts (FR-10) | ⚠️ Partial | Preferences persist; **domain data (students, grades, etc.) does not reliably persist**, since it lives only in memory plus a best-effort remote sync |
| Full admin ERP portal | ✅ Surprisingly extensive | Despite being "out of scope for initial foundation" per the PRD, a large CRUD surface exists: Students, Faculty, Classes, Sections, Grades, Announcements, Events, Timetable, Attendance marking, Reports — more built than the PRD scoped |
| Real payments | ❌ Not implemented | UI references only (Fees screen doesn't exist) |
| Real-time chat | ❌ Not implemented | "Messages" is a dead link; `ChatMessage` model exists but is only used for the AI assistant, not human-to-human messaging |
| Production push notifications | ❌ Not implemented | No FCM/APNs integration; `POST_NOTIFICATIONS` permission is only checked for status display, never requested/used to actually send anything |
| Multi-school tenancy | ❌ Not implemented | No school/tenant ID anywhere in the schema or DTOs |

**AI Assistant (not in original PRD, but present):** A genuinely interesting addition — a Gemini-backed chat assistant with a "detect intent → fetch grounding context from the repository → answer" pattern (reasonable "reduce hallucination" design) and a confirm-before-write flow for destructive actions (good instinct). But the intent detection is pure keyword/substring matching (`query.contains("attendance")`), which is brittle (fails on typos, synonyms, multi-intent messages, non-English phrasing) and the write-confirmation payloads are hardcoded rather than actually extracted from the message (§2.6/#4).

---

## 5. Code Quality

**Positives:**
- Consistent naming and package structure per feature.
- DTO ↔ domain mapping is separated (DTOs aren't leaked to UI), matching the project's own architecture doc.
- Compose screens are reasonably decomposed into smaller composables (e.g. `RoleSelectionContent`, `CredentialsInputContent`) rather than one giant function.
- A real, if small, shared component library exists (`AppButton`, `AppCard`, `AppTextField`, `AppTopBar`, empty/error/loading states) — this is the right instinct for a Compose app and keeps a lot of screens visually consistent.

**Negatives:**
- **Zero real automated test coverage.** Only the two default Android Studio template tests exist, under the wrong (pre-rename) package, testing nothing about this app.
- **Repeated boilerplate across every management repository method** — `addX/updateX/deleteX` for Student, Faculty, Class, Section, Grade, Announcement, Event, Timetable, Assignment are all near-identical copy/pasted blocks (mutate list → fire-and-forget Supabase call → swallow error). This is exactly the "copy/paste implementations" the project's own `rules.md` warns against, and it's a maintenance risk: a fix to error handling has to be repeated ~9 times by hand.
- Soft-deletes (`isActive = false`) are used for students/faculty/classes/etc., but hard `delete` is used for assignments — inconsistent deletion semantics with no documented reasoning.
- No centralized error/result type beyond `kotlin.Result` — no distinction between "network unavailable," "unauthorized," "validation failed," which `Architecture.md` itself calls for.
- Silent `catch (e: Exception) { }` blocks (many, throughout the data layer) are a textbook anti-pattern: failures are invisible to both the user and to any crash/analytics tooling.
- Dead widget code (`Tx.kt`) and dead routes (`Fees`, `Messages`) indicate no cleanup pass before delivery.
- Release build has `isMinifyEnabled = false` and an empty ProGuard file — larger APK, no obfuscation.
- `local.properties` (which should be per-developer/untracked) was included as an actual file in the source tree rather than excluded — worth double-checking `.gitignore` coverage.

---

## 6. Performance & Scalability

- Every "list" screen fetches with `supabasePostgrest.from("table").select()` — **no pagination, no field selection, no server-side filtering** visible anywhere. This is fine for a 5-row demo table; it will not scale to a real school's data (hundreds–thousands of students, years of attendance/grade history). Every screen load would pull the *entire* table into memory.
- All domain data lives in a handful of app-wide `MutableStateFlow`s inside singleton repositories — memory usage grows unbounded with data size, and there's no cache eviction or lazy loading (e.g. `LazyColumn` `PagingSource` isn't used anywhere despite being the natural fit for Compose + large lists).
- No local database (Room) means every cold start either shows stale in-memory defaults or has to wait on a full network fetch with no visible loading state tied to it (`syncInitialDataWithSupabase()` runs detached from any UI state).
- No image/document handling infrastructure at all — no student photo upload, no assignment file attachments, no report-card PDF export — despite a school system being a very document-heavy domain.
- Realtime is installed but unused (see bug #7) — meaning even the one piece of infrastructure that would give live updates across devices without polling is sitting idle.

---

## 7. UI/UX

- The design system doc (`Design.md`) is thoughtful and specific (clear hierarchy rules, "avoid neon/gradients," consistent empty/loading/error states, accessibility notes) — better documentation than most real projects have.
- In practice, many screens (Notifications, Home schedule/announcements) are backed by **hardcoded arrays defined inside the composable/viewmodel file itself**, not the repository layer, which contradicts the project's own "no fake functionality, no mock UI-only values" rule and will make these screens diverge from real data as soon as a backend exists.
- The login flow (role-first, then credentials) is a reasonable pattern for a demo, but as discussed, letting the *user* pick their portal is a security anti-pattern for anything beyond a prototype — normal school software determines role from the account after authentication, not before.
- No onboarding/tutorial flow, no forgot-password flow (only the broken local "change password" exists), no account-verification email step surfaced in the UI even though Supabase Auth supports it.
- Accessibility: components use Material 3 defaults, which is a reasonable baseline, but there's no evidence of tested TalkBack labels, dynamic type scaling checks, or contrast audits — the design doc asks for these but nothing in the code confirms they were verified.

---

## 8. What's Missing for a Real, Sellable School Management Product

Beyond fixing the above, a production system in this space typically needs:

**Core gaps**
- A real backend authorization model (RLS policies, or a dedicated backend service) — not client-trusted roles.
- Parent accounts genuinely linked to one or more student records (with consent), not just a persona in a requirements doc.
- Fees/payments: invoicing, online payment gateway integration (Razorpay/Stripe/PayU depending on target market), receipts, dues reminders.
- Real messaging: teacher↔parent/student threads, likely backed by Supabase Realtime (already installed!) or a purpose-built chat service.
- Push notifications via FCM for assignments, attendance alerts, announcements, fee due dates — the permission is checked but nothing sends anything.
- Bulk operations: CSV/Excel import for student rosters, bulk attendance marking, bulk grade entry — currently everything is one-record-at-a-time.
- Document generation: report cards / transcripts / ID cards as PDF export.
- Timetable conflict detection (double-booked teacher/room) — currently `AddEditTimetableScreen` appears to allow any combination with no validation against overlaps.
- Multi-school/tenant support if this is ever sold to more than one institution.
- An actual admin **web** console — mobile-only administration is a real friction point for office staff doing bulk data entry.

**Operational/compliance gaps**
- Audit logging (who changed a grade, who marked attendance, when) — essential for a system of record schools will be legally scrutinized over.
- Data retention/export/delete flows for compliance with FERPA (US), GDPR (EU), or India's DPDP Act, depending on target market — especially important since this handles children's data.
- Backup/disaster-recovery story beyond "Supabase's own backups."
- Terms of service/privacy policy surfaced in-app (none seen in the UI).

---

## 9. Recommended Modern Additions (if continuing to build this out)

1. **Move authorization server-side properly**: Supabase Row Level Security policies keyed off `auth.uid()` and a `profiles`/`user_roles` table; consider Supabase Edge Functions for anything that needs to run with elevated privileges (e.g., admin-only bulk operations, the Gemini proxy).
2. **Add Room (or SQLDelight) as a real local cache/offline layer**, with the existing repository interfaces staying the same — this is exactly the seam the project's own `Architecture.md` describes, it's just not implemented yet for domain data (only DataStore for simple prefs exists today).
3. **Adopt Paging 3** for all list screens (`LazyColumn` + `PagingSource` backed by Supabase range queries) once real data volume exists.
4. **Turn on Supabase Realtime subscriptions** (already a dependency!) for announcements/attendance/grades so multiple devices (parent + student + teacher) see updates live instead of only at app-open.
5. **Move the Gemini calls behind a Supabase Edge Function**, so the API key never ships in the client, and add per-user rate limiting there to control cost.
6. **Firebase Cloud Messaging** (or Supabase's own push integration) for real push notifications, replacing the currently-decorative permission check.
7. **WorkManager** for background sync/retry instead of today's fire-and-forget coroutines with silently swallowed failures.
8. **Biometric library (`androidx.biometric`)** to make the existing Security toggle actually do something.
9. **A proper testing pyramid**: JUnit + MockK/Turbine for ViewModels and repositories, Compose UI tests for critical flows (login, mark attendance, add student), and — given RLS is central to security here — SQL-level policy tests in Supabase (pgTAP or equivalent) so a bad policy change is caught in CI, not in production.
10. **CI/CD**: GitHub Actions running lint + unit tests + a debug build on every PR; nothing like this exists in the repo today (no `.github/workflows`).
11. **Analytics/crash reporting** (Firebase Crashlytics + Analytics, or a privacy-respecting alternative) — right now, network/data failures are invisible even to the developers, let alone the user.
12. If this needs to reach non-Android users, Kotlin Multiplatform is a natural extension path since domain/data layers are already fairly UI-agnostic Kotlin — but that's a significant strategic decision, not a quick add-on.

---

## 10. Prioritized Action Plan

**P0 — Do before any real users touch this:**
1. Remove the authentication bypass and client-selected role (§2.1, §2.2).
2. Stop storing/using the plaintext local password; wire "change password" to real Supabase Auth (§2.3).
3. Fix the two dead routes (Fees, Messages) — either implement minimal real screens or remove the entry points so the app can't crash.
4. Turn off `allowBackup` or add real exclusion rules (§2.4).
5. Move the Gemini key server-side; stop shipping it in the APK (§2.5).
6. Write and test Supabase RLS policies for every table currently accessed from the client.

**P1 — Needed for a genuinely working demo/pilot:**
7. Replace silent `catch {}` blocks with surfaced errors + retry, and add a real local cache (Room) so data survives restarts reliably.
8. Fix the AI assistant's hardcoded delete-target bug; tighten its write-permission model to match the corrected role system.
9. Add the Parent role and a real student-linkage model.
10. Add basic automated tests for auth, role handling, and the repository layer (highest risk areas).

**P2 — Needed to be competitive as a product:**
11. Fees/payments, real messaging, push notifications, bulk import/export, PDF report cards, timetable conflict checks, audit logs, a web admin console.

---

## Summary

The project has a legitimate, fairly clean Compose/Hilt/Supabase architecture on the surface, more admin CRUD screens than its own PRD asked for at this stage, and an interesting (if half-finished) AI assistant. But underneath, the authentication system can be bypassed by anyone, roles are self-granted with no server enforcement, a real password is stored in plaintext while a *fake* password field is what "change password" actually edits, two navigation entries will crash the app, and several "backend-integrated" features are actually in-memory mocks with best-effort, error-swallowing sync calls. None of this is unusual for an early-stage/AI-assisted prototype — but every one of these is also explicitly called out as forbidden in the project's *own* `rules.md`, which suggests the fastest path forward is enforcing that document against the actual code, starting with the P0 list above, before adding any new feature surface.