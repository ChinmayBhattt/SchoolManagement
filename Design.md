# TX EduSphere — Design System

## 1. Design Direction

TX EduSphere should feel like a polished, trustworthy school-management application.

Visual characteristics:
- Clean
- Professional
- Calm
- Modern
- Information-focused
- Light visual hierarchy
- Minimal unnecessary decoration

Avoid:
- Excessive gradients
- Neon effects
- Overly large illustrations
- Dense dashboards
- Excessive rounded cards
- Decorative UI that competes with school information

## 2. Existing Navigation

Do not redesign the existing bottom navigation unless explicitly requested.

Primary destinations:
1. Home
2. Explore
3. Notifications
4. Profile

The selected tab must always clearly communicate the current location.

## 3. Layout

Use:
- Consistent horizontal page padding.
- Comfortable vertical spacing.
- Clear section separation.
- Scrollable content where necessary.
- Safe-area awareness.
- Responsive sizing.

Recommended hierarchy:

Screen
→ Header
→ Primary information
→ Sections
→ Secondary actions
→ Supporting information

## 4. Typography

Use the project's existing typography system.

Hierarchy:
- Screen title: strong and prominent
- Section title: medium/semibold
- Card title: medium
- Body: regular
- Supporting metadata: smaller and lower emphasis

Never solve hierarchy only by increasing font size. Use weight, spacing, and contrast.

## 5. Color

Use the existing TX EduSphere palette from the application.

Rules:
- Preserve the existing primary brand color.
- Use neutral backgrounds.
- Use semantic colors only where meaningful.
- Success, warning, error, and informational states must remain distinguishable.
- Do not introduce a new color system for a single screen.

## 6. Components

Prefer reusable components for:
- App header
- Section header
- Summary card
- List item
- Settings item
- Notification item
- Empty state
- Error state
- Loading state
- Dialog
- Form field
- Primary button
- Secondary button
- Chip/filter
- Progress indicator

## 7. Home Design

Sections:
- Greeting
- Quick overview
- Today's schedule
- Announcements
- Upcoming events
- Quick actions

The most important information must appear first.

## 8. Explore Design

Structure:
- Header
- Search
- Categories
- Module cards
- Recently used

Module cards should be compact and scannable.

## 9. Notifications Design

Use grouped sections:
- Today
- Yesterday
- Earlier

Unread notifications should have a subtle visual distinction.

Filters should be easy to understand and use.

## 10. Profile Design

Structure:
- Profile identity
- Edit Profile
- Academic overview
- Personal information
- Account & preferences
- School information
- Support
- Logout

Settings rows should have:
- Icon
- Title
- Supporting text
- Navigation indicator/toggle when relevant

## 11. Forms

Forms must:
- Have visible labels.
- Validate input.
- Explain errors.
- Preserve entered data when validation fails.
- Disable submission while saving.
- Show success/error feedback.

## 12. States

Every data-driven component should consider:
- Loading
- Content
- Empty
- Error
- Disabled

Do not leave blank screens when data is unavailable.

## 13. Interaction

Use subtle animations only when useful:
- Navigation transitions
- Press feedback
- Expand/collapse
- Loading transitions

Avoid animation for decoration.

## 14. Accessibility

- Adequate touch targets.
- Meaningful content descriptions.
- Sufficient contrast.
- Do not communicate state using color alone.
- Support system font scaling where practical.
- Avoid tiny metadata text.

## 15. Design Rule

If a design decision makes the screen look more impressive but makes school information harder to understand, choose clarity.
