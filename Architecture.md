# TX EduSphere — Architecture

## 1. Architecture Goal

Build a modular Android application in which UI, business logic, persistence, and network/data sources are separated.

The architecture must allow the current mock/local implementation to evolve into a production backend without tightly coupling screens to APIs.

## 2. Recommended Layers

### Presentation
Responsible for:
- Screens
- UI state
- User interaction
- Navigation
- ViewModels/state holders

### Domain
Responsible for:
- Business models
- Use cases
- Repository interfaces
- Business rules

### Data
Responsible for:
- Repository implementations
- Local storage
- Remote API
- DTOs
- Mappers
- Data synchronization

## 3. Dependency Direction

Preferred dependency direction:

Presentation → Domain ← Data

Presentation should not directly depend on:
- Retrofit/API implementation
- Database implementation
- SharedPreferences/DataStore implementation details

Data implements domain repository contracts.

## 4. Suggested Structure

Adapt this to the existing project rather than blindly creating duplicate packages:

```text
app/
└── src/main/java/com/tx/edusphere/
    ├── core/
    │   ├── navigation/
    │   ├── theme/
    │   ├── ui/
    │   ├── result/
    │   └── utils/
    │
    ├── domain/
    │   ├── model/
    │   ├── repository/
    │   └── usecase/
    │
    ├── data/
    │   ├── local/
    │   ├── remote/
    │   ├── repository/
    │   ├── dto/
    │   └── mapper/
    │
    └── presentation/
        ├── home/
        ├── explore/
        ├── notifications/
        └── profile/
```

## 5. Navigation

Use one authoritative navigation system.

Rules:
- Define destinations centrally.
- Do not duplicate routes.
- Preserve bottom-navigation state.
- Use nested/detail destinations only when required.
- Clear the authenticated back stack after logout.

## 6. State Management

Each feature should have a clear UI state.

Example:

```text
UiState
├── Loading
├── Success(data)
├── Empty
└── Error(message)
```

For forms, maintain:
- Field values
- Validation state
- Submission state
- Error state
- Success state

## 7. Repository Pattern

Example conceptual contract:

```text
ProfileRepository
- getProfile()
- updateProfile(profile)
- getPreferences()
- updatePreferences(preferences)
```

The presentation layer calls a use case or domain contract, not the database/API directly.

## 8. Persistence

Use the existing project's persistence solution if one exists.

For simple preferences:
- DataStore is preferred over legacy key/value storage.

Potential persisted data:
- Theme
- Language
- Notification preference
- Privacy preferences
- Local profile edits
- Authentication/session state where appropriate

Never store passwords as plain text.

## 9. Remote Data

When backend integration is introduced:
- Keep API DTOs inside data.
- Map DTO → domain model.
- Do not expose DTOs directly to UI.
- Handle HTTP/network errors centrally.
- Add authentication headers through the appropriate network layer.

## 10. Mock Data

Mock implementations should implement the same repository interfaces as production implementations.

This allows:

```text
UI → UseCase → Repository Interface
                     ├── MockRepository
                     └── ApiRepository
```

## 11. Error Handling

Use a consistent result/error model.

Errors may include:
- Validation
- Network unavailable
- Unauthorized
- Server error
- Storage failure
- Feature unavailable

UI should receive user-safe messages rather than raw stack traces.

## 12. Security

- Never log passwords or tokens.
- Never hardcode secrets.
- Never commit API keys.
- Never store passwords in plaintext.
- Use secure storage mechanisms for sensitive credentials/tokens.
- Logout must clear session state appropriately.

## 13. Testing Strategy

### Unit tests
- Use cases
- Validators
- Repository logic
- Mappers

### UI tests
- Navigation
- Forms
- Settings toggles
- Notification filtering
- Logout flow

### Integration tests
- Repository/data-source integration where practical

## 14. Architecture Rules

- Reuse existing architecture before introducing a new pattern.
- Do not create a second navigation system.
- Do not create duplicate models.
- Do not call APIs directly from composables/views.
- Do not put business logic inside UI rendering code.
- Keep feature-specific code close to the feature.
