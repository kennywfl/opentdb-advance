# AGENTS.md — opentdb-advance

## Project structure

- **Two modules**: `:app` (Android application, UI layer) and `:lib` (Android library, data layer).
- **Entrypoint**: `LauncherActivity` → `launcher_graph` → `LauncherFragment`. Main navigation happens in `main_graph` (MainFragment, CatalogFragment, QuizFragment, dialogs) and `setting_graph` (SettingFragment).
- **Architecture**: MVP with Jetpack Compose UI. Presenters in `app/.../framework/presenter/`, Views in `app/.../framework/view/`. Fragments return `ComposeView` from `onCreateView`. Single `QuizViewModel` (LiveData-based) shared across the quiz flow.
- **UI**: All screen layouts are Jetpack Compose (Material3). Compose screens in `ui/screen/`. The only remaining XML layouts are activity shells hosting `NavHostFragment` and RecyclerView item views used by legacy adapters.
- **DI**: Dagger 2 with `DaggerApplication`. Component: `AppComponent`. Injector modules: `ActivityInjectorModule`, `FragmentInjectorModule`, `DialogFragmentInjectorModule`. Scopes: `ActivityScope`, `FragmentScope`, `DialogFragmentScope`.
- **Networking**: Retrofit 2 + RxJava 2 (`Observable`) + Gson + OkHttp. API: Open Trivia Database (`https://opentdb.com/`). Endpoints in `ApiMethod`, service in `ApiService`.
- **Navigation**: Android Navigation Component with Safe Args (`navigation-safeargs-kotlin` plugin). Nav graphs: `launcher_graph.xml`, `main_graph.xml`, `setting_graph.xml`.

## Build & run

```sh
./gradlew assembleDebug          # full debug build
./gradlew :app:assembleDebug      # build app module only
./gradlew :lib:assembleDebug      # build lib module only
./gradlew clean                   # clean build artifacts
```

There are **no tests** (no test sources exist). No CI is configured.

## Key versions

| Tool | Version |
|------|---------|
| Gradle | 8.13 |
| AGP | 8.13.1 |
| Kotlin | 2.1.20 |
| Java | 21 (source + target compat) |
| compileSdk / targetSdk | 36 |
| minSdk | 26 |
| Compose BOM | 2025.01.01 |
| Compose compiler | Kotlin plugin (`org.jetbrains.kotlin.plugin.compose`) |

## Framework quirks

- **UI is Jetpack Compose Material3**. Theme in `ui/theme/` (`Color.kt`, `Type.kt`, `Theme.kt`). Activity shells use XML (minimal `FragmentContainerView` for `NavHostFragment`); all fragment/dialog content is Compose via `ComposeView`.
- **ViewBinding** is still enabled for the remaining activity and view-item XML layouts.
- **Unsure OkHttp**: `Constants.USE_SSL = false` — the app builds an unsafe OkHttp client that trusts all certificates.
- **Constants.IS_MOCK = false** — no mock mode.
- **Logging**: Timber — `DebugTree` in debug builds (`lib/src/debug/`), `ReleaseTree` in release builds (`lib/src/release/`).
- **Dark mode**: Controlled via `AppCompatDelegate.setDefaultNightMode` in `BaseApplication.onCreate()`, persisted in `AppSharedPreference`. Compose `AppTheme` uses `isSystemInDarkTheme()`.
- **Release builds**: `minifyEnabled false` — no ProGuard/R8 shrinking.
- **API token handling**: `DataManager` auto-requests, resets, and persists a session token via `AppSharedPreference`. Response codes 3 (token not found) and 4 (token exhausted) trigger automatic recovery.
- **Paging**: Uses `androidx.paging` (version 2.1.0) with RxJava2 adapter and `QuestionListDataSource` — RecyclerView adapters remain, hosted inside `AndroidView` in Compose.

## Package layout

```
app/src/main/java/com/opentrivia/app/
├── activity/          # Activities (LauncherActivity, MainActivity, SettingActivity, BaseActivity)
├── adapter/           # RecyclerView adapters + paging DataSource
├── dialogfragment/    # QuickQuizDialogFragment, ResultDialogFragment
├── fragment/          # UI fragments (return ComposeView)
├── framework/
│   ├── model/         # QuizViewModel
│   ├── presenter/     # MVP presenters
│   └── view/          # MVP view interfaces
├── injection/
│   ├── component/     # AppComponent
│   └── module/        # DI modules (AppModule, ActivityModule, FragmentModule, etc.)
├── ui/
│   ├── screen/        # Composable screen functions (LauncherScreen, MainScreen, etc.)
│   └── theme/         # Compose theming (Color, Type, Theme)
├── BaseApplication.kt
└── util/

lib/src/main/java/com/opentrivia/app/lib/
├── datasource/
│   ├── local/sharedpreference/  # SharedPref wrappers
│   ├── model/                   # QuestionCount, Questions
│   └── remote/
│       ├── mapping/request/     # API request DTOs
│       ├── mapping/response/    # API response DTOs
│       ├── network/             # BaseNetworkHelper, ApiNetworkHelper, ApiMethod
│       └── service/             # ApiService (Retrofit interface)
├── extension/          # RxJava observer extensions
├── framework/          # DataObserver interface
├── injection/
│   ├── module/         # NetworkModule
│   ├── qualifier/      # @ApplicationContext
│   └── scope/          # Custom scopes
├── Constants.kt
└── DataManager.kt
```

## Dependencies catalog

All dependency versions are declared in `gradle/libs.versions.toml` (version catalog). The legacy `dependencies.gradle` file at the root is unused — do not edit it. Add/update dependencies in the `.toml` file only. Compose deps are managed by BOM and bundled as `libs.bundles.compose`.
