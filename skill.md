# TX EduSphere — AI Development Skill

## Purpose

This document defines how an AI coding agent should work on the TX EduSphere Android project.

The agent must behave like a careful senior Android engineer: inspect first, understand the existing system, make focused changes, verify them, and avoid breaking working functionality.

## 1. Workflow

For every task:

### Step 1 — Understand
- Read the task carefully.
- Identify the exact requested feature.
- Identify affected screens/files.
- Check whether the feature already partially exists.

### Step 2 — Inspect
Inspect:
- Project tree.
- Relevant screen.
- Navigation.
- Theme.
- Components.
- ViewModel/state.
- Repository/data layer.
- Existing dependencies.

### Step 3 — Plan
Before editing, determine:
- Which existing files can be reused.
- Which new files are necessary.
- Which navigation destination is required.
- Where state should live.
- How persistence should work.

### Step 4 — Implement
Implement the smallest complete solution.

### Step 5 — Integrate
Connect:
- UI
- state
- navigation
- persistence
- repository/use case where appropriate

### Step 6 — Verify
Check:
- Compilation.
- Navigation.
- State updates.
- Persistence.
- Error states.
- Back behavior.
- Existing features.

## 2. Never Assume

Do not assume:
- A dependency exists.
- A route exists.
- A repository exists.
- A backend exists.
- A component exists.
- A screen is unused.

Inspect the code first.

## 3. UI Implementation

When a screen already exists:
- Preserve its current visual identity.
- Add functionality without unnecessary redesign.
- Reuse existing components.
- Keep content readable and scannable.

## 4. Functional Implementation

A feature is not complete when its UI is visible.

A feature is complete when:
1. The user can interact with it.
2. State changes correctly.
3. Navigation works.
4. Data is persisted when required.
5. Errors are handled.
6. The application remains stable.

## 5. Profile Feature Skill

When working on Profile:
- Treat each visible row as a real feature.
- Inspect whether a destination already exists.
- Implement missing destinations.
- Persist preferences.
- Validate forms.
- Confirm logout behavior.
- Never leave dead settings.

## 6. Data Flow

Prefer:

```text
User Action
    ↓
Presentation / ViewModel
    ↓
Use Case
    ↓
Repository Interface
    ↓
Local or Remote Data Source
    ↓
Domain Result
    ↓
UI State
    ↓
UI
```

Avoid:

```text
UI → API
UI → Database
UI → raw storage
```

## 7. Mock-First Development

When backend APIs are unavailable:
- Use a mock/local repository.
- Keep the interface identical to the future production repository.
- Make UI behavior real.
- Do not fake server responses through arbitrary UI code.

## 8. Persistence Skill

For user preferences:
- Read saved state when screen/app starts.
- Update persistence when the user changes a setting.
- Update UI immediately.
- Handle storage failure gracefully.

## 9. Navigation Skill

Before adding a route:
- Search for an existing equivalent.
- Reuse it if present.
- Add one canonical route if missing.

After adding a route:
- Test forward navigation.
- Test back navigation.
- Test bottom-navigation behavior.
- Test logout/back-stack behavior.

## 10. Debugging Skill

When something fails:

1. Read the exact error.
2. Identify the first meaningful cause.
3. Inspect surrounding code.
4. Fix the root cause.
5. Rebuild.
6. Re-test the affected flow.

Do not blindly change multiple unrelated files.

## 11. Quality Checklist

Before declaring a task complete:

### Architecture
- [ ] Existing architecture respected.
- [ ] No unnecessary dependency added.
- [ ] No duplicated system introduced.

### UI
- [ ] Existing design preserved.
- [ ] Responsive layout.
- [ ] Loading state considered.
- [ ] Empty state considered.
- [ ] Error state considered.

### Functionality
- [ ] Interaction works.
- [ ] State updates.
- [ ] Navigation works.
- [ ] Persistence works where required.

### Security
- [ ] No secrets committed.
- [ ] No passwords logged.
- [ ] No plaintext password storage.

### Regression
- [ ] Existing bottom navigation still works.
- [ ] Existing screens still open.
- [ ] No unrelated behavior changed.

### Verification
- [ ] Build/compile checked.
- [ ] Critical flow manually or automatically tested.

## 12. Stop Conditions

Do not claim completion if:
- Build is failing.
- A visible button is still dead.
- Navigation points to a missing destination.
- A setting appears to save but does not persist when persistence is required.
- Logout does not clear the session/back stack.
- A requested backend feature is falsely represented as working.

Instead, clearly identify what is implemented and what dependency is missing.

## 13. Prime Directive

**Inspect → Plan → Implement → Integrate → Verify.**

Never optimize for the number of files changed. Optimize for a correct, maintainable, production-ready result.
