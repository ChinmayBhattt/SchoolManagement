# TX EduSphere — AI Coding Rules

These rules apply to every AI-assisted change in this project.

## 1. Inspect Before Editing

Before changing code:
1. Inspect the existing project structure.
2. Identify the current architecture.
3. Find existing navigation.
4. Find existing theme/components.
5. Find existing state management.
6. Reuse existing implementations whenever possible.

Never assume a file or package does not exist.

## 2. Do Not Break Existing Features

Do not:
- Remove working navigation.
- Replace the existing bottom navigation unnecessarily.
- Rewrite unrelated screens.
- Change the visual design without being asked.
- Remove existing data models without checking usages.

## 3. Minimal Change Principle

Make the smallest clean change that fully solves the task.

Avoid:
- Large unrelated refactors.
- New dependencies without justification.
- Duplicate utility classes.
- Duplicate navigation routes.
- Duplicate components.

## 4. Existing Design Is the Source of Truth

When implementing a new feature:
- Match existing colors.
- Match existing typography.
- Match existing spacing.
- Match existing components.
- Match existing interaction patterns.

Do not invent a completely new design language.

## 5. No Fake Functionality

Never create:
- Buttons that do nothing.
- Fake success messages.
- Fake API calls.
- Pretend password changes.
- Fake backend synchronization.
- Dead navigation routes.

If backend functionality does not exist:
- Build a proper abstraction.
- Use local/mock persistence where appropriate.
- Clearly isolate future backend integration.

## 6. Profile Requirements

Every existing Profile option must either:
- Work genuinely, or
- Have a clearly structured implementation boundary for unavailable backend functionality.

Options include:
- Edit Profile
- Notifications
- Language
- Theme
- Privacy
- Security
- School Information
- Help Center
- Contact School
- Report a Problem
- Logout

## 7. State Persistence

Settings that users change must remain correct after:
- Leaving the screen.
- Navigating back.
- Reopening the app.

## 8. Navigation Rules

- Use the existing navigation system.
- Do not create duplicate routes.
- Back navigation must work.
- Do not trap users in screens.
- Logout must clear the authenticated back stack.

## 9. Security Rules

Never:
- Hardcode passwords.
- Log passwords.
- Store passwords as plaintext.
- Commit secrets/API keys.
- Put credentials in UI code.

## 10. Error Handling

Every async operation should account for:
- Loading
- Success
- Failure

Forms must validate user input before submission.

## 11. Code Quality

Prefer:
- Small focused classes/functions.
- Meaningful names.
- Immutable UI state where practical.
- Reusable components.
- Clear separation of responsibilities.

Avoid:
- Giant screens.
- God classes.
- Magic strings scattered through the code.
- Copy/paste implementations.

## 12. Dependencies

Before adding a dependency:
1. Check whether the project already has equivalent functionality.
2. Check whether an existing library can solve the requirement.
3. Add a new dependency only when it provides meaningful value.

## 13. Verification

After changes:
- Compile/build the affected module.
- Resolve compilation errors.
- Check navigation.
- Check state persistence.
- Check major interactions.
- Check for crashes.
- Review modified files for accidental unrelated changes.

## 14. Communication

When a requested feature cannot be fully implemented because a backend/service is missing:
- Do not pretend it works.
- Explain the limitation.
- Implement the cleanest frontend/local abstraction possible.
- Identify the exact integration point for future backend work.

## 15. Golden Rule

**Do not change what is already working unless the task explicitly requires it.**
