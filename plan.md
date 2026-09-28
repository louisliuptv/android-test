# Implementation Plan

Derived from `AGENTS.md`: MVI, clean architecture, and Gradle modularization.

## Architecture Baseline

- **MVI** per screen: `StateFlow` for UI state, `SharedFlow` for one-off events, `Channel` for side effects (navigation, snackbar).
- **Clean architecture** per feature: `domain` (repository interface + use cases), `data` (api service, datasource, repository impl), `presenter` (Compose screen + ViewModel).
- **Modules**:
  - `:core:common` - base MVI ViewModel, `ApiResult`, `ApiError` / user-facing messages, dispatchers, token contracts.
  - `:core:network` - Retrofit, OkHttp, interceptors, auth/refresh flow.
  - `:core:design-system` - Material 3 theme + reusable UI components.
  - `:app` - single `MainActivity`, Compose nav graph, DI wiring.
  - `:feature:auth` - login.
  - `:feature:gps` - GPS list + detail.
- **Stack**: Kotlin, Jetpack Compose, Hilt (DI), Retrofit/OkHttp, Kotlinx Serialization or Moshi, Coroutines, JUnit5 + MockK + Turbine + Compose UI test.
- **Existing baseline**: AGP 8.7.3, Kotlin 2.0.21, Gradle 8.13, compileSdk 35, minSdk 24, JDK 17.

## Dependency Graph

```
:app ──> :feature:auth ──> :core:common
     │                 └─> :core:network ──> :core:common
     ├──> :feature:gps  ──> :core:common, :core:network, :core:design-system
     └──> :core:design-system
```
Rules: features never depend on each other; `domain` has no Android/Retrofit imports; `data` depends on `domain`; only `:app` composes features.

---

## Phase 1 - Environment & Repository Setup

Goal: reproducible module skeleton and version control.

1. Init git (`git init`), add `.gitignore` (already present), first commit on `main`.
2. Configure Gradle:
   - Add `gradle/libs.versions.toml` version catalog (Kotlin, AGP, Compose BOM, Hilt, Retrofit, OkHttp, coroutines, test libs).
   - Create a `build-logic/` included build with convention plugins:
     - `emptyapp.android.library`, `emptyapp.android.feature`, `emptyapp.android.application`, `emptyapp.android.hilt`, `emptyapp.android.compose`, `emptyapp.kotlin.library`.
   - Enable `plugins { ... }` via `includeBuild("build-logic")` in `settings.gradle.kts`.
3. Create module directories and register in `settings.gradle.kts`:
   - `:core:common`, `:core:network`, `:core:design-system`, `:feature:auth`, `:feature:gps`.
   - Standard source sets per module: `src/main`, `src/test`, `src/androidTest` (Android modules).
4. Create each module's `build.gradle.kts` applying the right convention plugin and declaring dependencies per the graph.
5. Add package `com.example.emptyapp.<module>` and empty `AndroidManifest.xml` where needed.
6. CI baseline: `./gradlew lint testDebugUnitTest` script (local + optional GitHub Actions).
7. Verification: `./gradlew projects` lists all modules; `./gradlew assembleDebug` stays green.

Deliverable: empty modules compile; commit tagged `phase-1-setup`.

---

## Phase 2 - `:core:common`

Goal: shared MVI base, result/error model, token contracts.

1. Coroutines: `DispatcherProvider` interface + `DefaultDispatcherProvider`; inject dispatchers (testable).
2. Result model:
   - `sealed interface ApiResult<out T>`: `Success(data)`, `Failure(ApiError)`.
   - `sealed interface ApiError`: `NoNetwork`, `Timeout`, `Unauthorized`, `Forbidden`, `BadRequest`, `NotFound`, `Server`, `Serialization`, `Unknown`.
   - `ApiError.toUiText()` / `ErrorMessageProvider` mapping error -> string resource id (no Android imports in `domain`; mapping lives in a `presenter`/common-Android source set).
3. MVI base:
   - `abstract class BaseMviViewModel<State : UiState, Event : UiEvent, Effect : UiEffect>(initialState)`.
   - `MutableStateFlow<State> uiState` exposed as `StateFlow`.
   - `MutableSharedFlow<Event> events` for UI events.
   - `Channel<Effect> effects` exposed as `Flow` for one-off side effects.
   - `fun setState(reducer: State.() -> State)` and `sendEffect(effect)`.
   - Safe `launch` helper catching exceptions into state/effects.
4. Token status contract (no persistence logic):
   - `sealed interface TokenStatus { object Authenticated; object Unauthenticated; data class Expired(...) }`.
   - `interface TokenProvider { fun currentAccessToken(): String?; suspend fun refreshToken(): TokenStatus }`.
   - `interface SessionManager { val sessionState: StateFlow<TokenStatus>; fun onTokenExpired() }`.
5. Unit tests: `ApiResult`/`ApiError` mapping, base ViewModel state/effect emission (Turbine).

Verification: `./gradlew :core:common:testDebugUnitTest`.

---

## Phase 3 - `:core:network`

Goal: Retrofit/OkHttp stack with auth, logging, and error translation.

1. OkHttp client assembly via Hilt `@Provides`:
   - `Timeouts` (connect/read/write), `HttpLoggingInterceptor` (**level `BODY` on debug, `NONE` in release** - decided by `BuildConfig.DEBUG` or a `NetworkConfig.isDebug`), redact `Authorization` headers.
   - `AuthInterceptor`: adds `Authorization: Bearer <accessToken>` from `TokenProvider`; skips auth endpoints.
   - `Authenticator` (401 handling): on `401`, call `TokenProvider.refreshToken()` once; on failure -> `SessionManager.onTokenExpired()` and emit `Unauthenticated`.
   - Optional `NetworkConnectivityInterceptor` / `CacheInterceptor` + offline cache.
   - `HeaderInterceptor` (User-Agent, Accept, App-Version).
2. Retrofit setup:
   - Base URL from `buildConfigField` per build type (debug/staging/release).
   - Converter (Kotlinx Serialization or Moshi), `CallAdapter` for `ApiResult<T>` wrapping.
   - `ApiCallAdapter` / `safeApiCall {}` translating exceptions to `ApiError` (see Phase 2): `UnknownHostException`/`ConnectException` -> `NoNetwork`, `SocketTimeoutException` -> `Timeout`, HTTP code mapping.
3. Error translation layer:
   - `HttpException -> ApiError` mapping: `400 BadRequest`, `401 Unauthorized`, `403 Forbidden`, `404 NotFound`, `5xx Server`, parse error body for server message when available.
   - Central `NetworkErrorMapper` used by both `safeApiCall` and repository layer.
4. Hilt module (`NetworkModule`) exposing `OkHttpClient`, `Retrofit`, `Json`, qualifiers for authenticated vs unauthenticated clients.
5. Token contract implementation binding placeholder until Phase 5 (`feature:auth`) provides the real store.
6. Unit tests: interceptor header injection, log-level selection per build type, `HttpException` -> `ApiError` mapping, authenticator refresh-once behavior (MockWebServer).

Verification: `./gradlew :core:network:testDebugUnitTest`.

---

## Phase 4 - `:core:design-system`

Goal: Material 3 theme and reusable components.

1. Theme: `Color.kt`, `Type.kt`, `Shape.kt`, `EmptyAppTheme` using Material 3 dynamic color (Android 12+) with light/dark fallback palettes.
2. Reusable components:
   - `LoadingIndicator`, `ErrorState` (message + retry), `EmptyState`, `PrimaryButton`, `AppTopBar`, `AppScaffold`, `TextField` wrapper, `SnackbarHost` helper.
   - A `UiStateContainer` composable that switches between Loading/Content/Error/Empty from a sealed UI state.
3. Theme previews (`@Preview` light/dark) and a component gallery screen for manual QA.
4. UI tests (Compose) for key components (state rendering, retry callback).

Verification: `./gradlew :core:design-system:testDebugUnitTest` (and instrumented when device available).

---

## Phase 5 - `:feature:auth` (Login)

Goal: login screen + secure token persistence.

1. Domain:
   - `AuthRepository` interface: `suspend fun login(username, password): ApiResult<Unit>`, `logout()`, `isLoggedIn`.
   - `LoginUseCase`, `LogoutUseCase`.
2. Data:
   - `AuthApiService` (`POST /auth/login`), DTOs, `AuthRepositoryImpl` using `safeApiCall`.
3. Secure token storage (three tiers):
   - **Keystore**: `TokenCipher` using `AndroidKeyStore` AES/GCM to encrypt tokens.
   - **Encrypted SP**: encrypted `EncryptedSharedPreferences`/`DataStore` holding ciphertext + IV; `EncryptedTokenDataSource`.
   - **In-memory**: `InMemoryTokenCache` (`StateFlow`/`AtomicReference`) implementing `TokenProvider` for fast access and the single source during a session.
   - `TokenStore` facade: writes through keystore->encrypted SP, reads into memory on app start; clears all on logout.
4. Presenter:
   - `LoginContract` (`State`, `Event`, `Effect`), `LoginViewModel : BaseMviViewModel`.
   - `LoginScreen` composable: username/password, validation, loading, error, success effect -> navigate to GPS list.
5. Bind `TokenProvider`/`SessionManager` implementations into Hilt (replaces Phase 3 placeholder).
6. Tests: `LoginViewModel` (validation, success/error), `TokenCipher`/`TokenStore` round-trip (Robolectric), logout clears all tiers.

Verification: `./gradlew :feature:auth:testDebugUnitTest`.

---

## Phase 6 - `:feature:gps` (List)

Goal: paginated GPS list.

1. Domain: `GpsRepository` interface (`getGpsList(page): ApiResult<List<GpsItem>>`), `GetGpsListUseCase`, `GpsItem` model.
2. Data: `GpsApiService` (`GET /gps?page=`), DTO + mapper to domain, `GpsRepositoryImpl`.
3. Presenter:
   - `GpsListContract` (`State` with items/page/isLoading/endReached/error), `Event` (load, refresh, loadMore, itemClick), `Effect` (navigate to detail).
   - `GpsListViewModel`: pagination, refresh, error/retry, dedupe.
   - `GpsListScreen` using design-system `UiStateContainer`, list + load-more footer; item click -> `NavigateToDetail(id)`.
4. Tests: paging/load-more, empty and error states, event/effect emission.

Verification: `./gradlew :feature:gps:testDebugUnitTest`.

---

## Phase 7 - `:feature:gps` (Detail)

Goal: GPS detail screen.

1. Domain: `getGpsDetail(id): ApiResult<GpsDetail>`, `GpsDetail` model, `GetGpsDetailUseCase`.
2. Data: `GET /gps/{id}`, DTO + mapper, repository method.
3. Presenter: `GpsDetailContract`, `GpsDetailViewModel` (load by id from nav args), `GpsDetailScreen` (loading/error/content + back).
4. Tests: load success/failure, invalid id handling.

Verification: `./gradlew :feature:gps:testDebugUnitTest`.

---

## Phase 8 - `:app` (Composition Root & Navigation)

Goal: single activity, navigation, DI wiring, auth gating.

1. `MainActivity : ComponentActivity`, `setContent { EmptyAppTheme { AppNavHost() } }`.
2. Navigation (Navigation-Compose) routes: `login`, `gps_list`, `gps_detail/{id}`; type-safe routes (`@Serializable` route objects).
3. Auth gating: start destination decided by `SessionManager.sessionState`; on session expiry effect, clear back stack -> `login`.
4. Effect handling: each screen's `Channel` effects collected in the screen composable (navigation/snackbar) via `LaunchedEffect`.
5. Hilt application class (`@HiltAndroidApp`), `AndroidManifest.xml` registers it.
6. Build types: `debug` (logging BODY), `release` (minify + logging NONE), base URLs per type.
7. Tests: nav graph instrumented test (login -> list -> detail, expiry -> login).

Verification: `./gradlew assembleDebug lint` and, with device/emulator, `./gradlew connectedDebugAndroidTest`.

---

## Cross-Cutting

- **Unit testing**: domain use cases, ViewModels (Turbine), error mappers, token store. Target meaningful coverage on `:core:common`, `:core:network`, ViewModels.
- **Error handling**: all failures funnel through `ApiError`; UI never sees exceptions, only `ApiResult`/state.
- **Security**: no tokens in plain SP/SharedPreferences/logs; `Authorization` header redacted in logs; keystore keys never exported.
- **Definition of Done** per phase: compiles, lint clean, unit tests green, no feature-to-feature dependency, small focused commit.

## Milestones

| Milestone | Phases | Outcome |
|-----------|--------|---------|
| M1 Skeleton | 1-2 | Modular project + shared core |
| M2 Networking | 3 | Auth/logging/error-safe network stack |
| M3 UI Kit | 4 | Material 3 theme + components |
| M4 Auth | 5 | Login with secure token storage |
| M5 GPS | 6-7 | List + detail |
| M6 Integration | 8 | Navigation, auth gating, release build |
