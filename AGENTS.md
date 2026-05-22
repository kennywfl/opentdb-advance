# AGENTS.md — opentdb-advance

## Project structure

- **Two modules**: `:app` (Android application, UI layer) and `:lib` (Android library, data layer).
- **Entrypoint**: `LauncherActivity` → starts `MainActivity` via Intent. `MainActivity` uses Nav3 `NavDisplay` with 5 composable entries (Main, Catalog, Quiz, QuickQuiz, Result).
- **Architecture**: MVVM with Jetpack Compose UI. ViewModels in `app/.../di/Modules.kt` (Koin `viewModelOf`) and `app/.../model/QuizViewModel.kt`. Compose screens in `ui/screen/`.
- **UI**: 100% Jetpack Compose (Material3). No Fragments, no XML layouts, no ViewBinding.
- **DI**: Koin via `startKoin` in `BaseApplication`. Modules: `networkModule` + `dataModule` in lib, `appModule` in app.
- **Networking**: Retrofit 2 + kotlinx-serialization converter + OkHttp. API: Open Trivia Database (`https://opentdb.com/`). Coroutines-based (`suspend fun` in `ApiService`).
- **Navigation**: Navigation 3 (`Nav3`). Single `MainActivity` uses `NavDisplay` with `rememberNavBackStack`. Routes defined as `NavRoute` sealed interface. Bottom bar + toolbar in `Scaffold`.

## Build & run

```sh
./gradlew assembleDebug          # full debug build
./gradlew :app:assembleDebug      # build app module only
./gradlew :lib:assembleDebug      # build lib module only
./gradlew clean                   # clean build artifacts
```

There are **no tests**. No CI is configured.

## Key versions

| Tool | Version |
|------|---------|
| Gradle | 8.13 |
| AGP | 8.13.1 |
| Kotlin | 2.3.20 |
| Java | 21 (source + target compat) |
| compileSdk / targetSdk | 36 |
| minSdk | 26 |
| Compose BOM | 2026.05.00 |
| Compose compiler | Kotlin plugin (`org.jetbrains.kotlin.plugin.compose`) |
| Koin | 4.2.1 (via BOM) |
| Navigation 3 | 1.0.0 |
| Retrofit | 2.12.0 |
| OkHttp | 4.12.0 |

## Framework quirks

- **UI is 100% Jetpack Compose Material3** — no XML layouts, no fragments, no ViewBinding.
- **Theme** in `ui/theme/` (`Color.kt`, `Type.kt`, `Theme.kt`). Uses `isSystemInDarkTheme()` — dark mode is separately toggled via `SettingActivity` which calls `AppCompatDelegate.setDefaultNightMode`.
- **Unsure OkHttp**: `Constants.USE_SSL = false` — the app builds an unsafe OkHttp client that trusts all certificates.
- **Constants.IS_MOCK = false** — no mock mode.
- **Logging**: Timber — `DebugTree` in debug builds (`lib/src/debug/`), `ReleaseTree` in release builds (`lib/src/release/`).
- **Dark mode**: Controlled via `AppCompatDelegate.setDefaultNightMode` in `BaseApplication.onCreate()`, persisted in `AppSharedPreference`. UI toggle in `SettingActivity` (Compose Settings screen).
- **Release builds**: `minifyEnabled false` — no ProGuard/R8 shrinking.
- **API token handling**: `DataManager` auto-requests, resets, and persists a session token via `AppSharedPreference`. Response codes 3 (token not found) and 4 (token exhausted) trigger automatic recovery.
- **QuizViewModel** is NOT in Koin — activity-scoped via `viewModel()` from `lifecycle-viewmodel-compose` (shared across Quiz/QuickQuiz/Result Nav3 entries via `LocalContext.current as ComponentActivity`).
- **AppSharedPreference** reads dark-mode preference from the default shared prefs file (matching `PreferenceManager.getDefaultSharedPreferences` semantics).

## Package layout

```
app/src/main/java/com/opentrivia/app/
├── activity/          # Activities (LauncherActivity, MainActivity, SettingActivity, BaseActivity)
├── di/                # Koin app module (viewModelOf for 3 ViewModels)
├── model/             # QuizViewModel (StateFlow-based)
├── ui/
│   ├── screen/        # Composable screen functions (LauncherScreen, MainScreen, CatalogScreen, QuizScreen, etc.)
│   └── theme/         # Compose theming (Color, Type, Theme)
└── BaseApplication.kt

lib/src/main/java/com/opentrivia/app/lib/
├── datasource/
│   ├── local/sharedpreference/  # SharedPref wrappers
│   ├── model/                   # QuestionCount, Questions
│   └── remote/
│       ├── mapping/request/     # API request DTOs (@Serializable)
│       ├── mapping/response/    # API response DTOs (@Serializable)
│       ├── network/             # BaseNetworkHelper, ApiNetworkHelper, ApiMethod
│       └── service/             # ApiService (Retrofit interface, suspend fun)
├── di/                # Koin modules (networkModule, dataModule)
├── Constants.kt
└── DataManager.kt     # Coroutines-based, no @Inject
```

## Dependencies catalog

All dependency versions are declared in `gradle/libs.versions.toml` (version catalog). Add/update dependencies in the `.toml` file only. Compose deps are managed by BOM and bundled as `libs.bundles.compose`.

## Migration history

The project was modernized from a legacy stack (Dagger, RxJava 2, Gson, MVP, Navigation 2, Paging 2, fragments) to a modern stack (Koin, coroutines, kotlinx-serialization, MVVM, Navigation 3, full Compose) across 8 phases:
1. Dependency upgrades (Kotlin 2.3.20, Compose BOM 2026.05.00, etc.)
2. Lib: Gson → kotlinx-serialization, RxJava → coroutines
3. Lib: Dagger → Koin
4. App: Dagger → Koin
5. App: MVP → MVVM (presenters → ViewModels), fragments → Composables
6. Navigation 2 → Navigation 3
7. Remaining XML → Compose (SettingActivity)
8. Removed legacy dependencies (Dagger, Gson, RxJava, Paging 2, Navigation 2 deps)
