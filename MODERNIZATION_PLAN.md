# Modernization Plan

**Goal**: Rewrite opentdb-advance with modern Android architecture:
MVVM + Compose-only UI + Kotlin Coroutines + Kotlinx Serialization + Navigation 3 + Koin

**Strategy**: Work bottom-up (lib → app), replacing one layer at a time, with a commit after each completed phase.

---

## Phase 1 — Dependencies & Build

| Step | Change | Files |
|------|--------|-------|
| 1.1 | Update `libs.versions.toml` with new libraries and remove old ones | `gradle/libs.versions.toml` |
| 1.2 | Update `app/build.gradle.kts` — add plugins, remove kapt/viewBinding/nav-safeargs | `app/build.gradle.kts` |
| 1.3 | Update `lib/build.gradle.kts` — add serialization plugin, remove kapt | `lib/build.gradle.kts` |
| 1.4 | Update `build.gradle.kts` — add serialization plugin (apply false) | `build.gradle.kts` |

**New deps added**: Koin BOM + android + compose, kotlinx-serialization plugin + json, kotlinx-coroutines, Navigation 3 runtime + ui, Retrofit kotlinx-serialization converter, lifecycle-viewmodel-compose

**Old deps removed**: Dagger, Gson, RxJava (rxandroid, rxkotlin, rxjava2-adapter), Navigation 2, Paging 2, Safe Args

---

## Phase 2 — Lib Module: Kotlinx Serialization + Coroutines

| Step | Change | Files |
|------|--------|-------|
| 2.1 | Add `@Serializable` to all response/request DTOs, remove Gson annotations | `lib/.../mapping/**/*.kt` |
| 2.2 | Update `ApiService` — replace `Observable<Response>` with `suspend fun` | `lib/.../service/ApiService.kt` |
| 2.3 | Update `BaseNetworkHelper` — replace RxJava adapter with kotlinx-serialization converter | `lib/.../network/BaseNetworkHelper.kt` |
| 2.4 | Rewrite `DataManager` — replace RxJava with suspend functions / Flow | `lib/.../DataManager.kt` |
| 2.5 | Update `AppSharedPreference` — replace Gson JSON with kotlinx-serialization JSON | `lib/.../sharedpreference/AppSharedPreference.kt` |
| 2.6 | Remove RxJava files: `DataObserver.kt`, `observer.kt` | `lib/.../framework/`, `lib/.../extension/` |

*Commit: `lib: migrate to kotlinx.serialization and coroutines`*

---

## Phase 3 — Lib Module: Koin DI

| Step | Change | Files |
|------|--------|-------|
| 3.1 | Replace `NetworkModule` (Dagger @Provides) with Koin module | `lib/.../injection/module/NetworkModule.kt` |
| 3.2 | Create app-level Koin module for `DataManager`, `AppSharedPreference` | `lib/.../injection/module/AppModule.kt` |
| 3.3 | Remove Dagger-specific files: scope annotations, qualifier | `lib/.../injection/scope/`, `lib/.../injection/qualifier/` |

*Commit: `lib: migrate DI from dagger to koin`*

---

## Phase 4 — App Module: Koin DI

| Step | Change | Files |
|------|--------|-------|
| 4.1 | Replace `BaseApplication` (DaggerApplication) with KoinApplication | `app/.../BaseApplication.kt` |
| 4.2 | Replace activity/fragment injections with Koin `by inject()` | All activity, fragment, dialog files |
| 4.3 | Remove all Dagger files: `AppComponent`, injector modules, scope annotations | `app/.../injection/` |
| 4.4 | Remove `BaseActivity`, `BaseFragment`, `BaseDialogFragment` Dagger base classes, simplify | `app/.../activity/`, `app/.../fragment/` |

*Commit: `app: migrate DI from dagger to koin`*

---

## Phase 5 — App Module: MVVM (ViewModels + StateFlow)

| Step | Change | Files |
|------|--------|-------|
| 5.1 | Create `QuizViewModel` — replace LiveData with StateFlow | `app/.../framework/model/QuizViewModel.kt` |
| 5.2 | Create ViewModels: `MainViewModel`, `CatalogViewModel`, `QuizViewModel`, `LauncherViewModel` | `app/.../framework/model/` |
| 5.3 | Remove all presenters and view interfaces | `app/.../framework/presenter/`, `app/.../framework/view/` |
| 5.4 | Update fragments to observe ViewModel StateFlow instead of presenter callbacks | All fragment files |

*Commit: `app: migrate from mvp to mvvm with viewmodels + stateflow`*

---

## Phase 6 — App Module: Navigation 3

| Step | Change | Files |
|------|--------|-------|
| 6.1 | Create route definitions (`NavKey` implementations) | `app/.../navigation/Routes.kt` |
| 6.2 | Create `NavigationState.kt` + `Navigator.kt` | `app/.../navigation/` |
| 6.3 | Replace nav graphs with `entryProvider` | Remove XML nav graphs, create composable entry provider |
| 6.4 | Replace `NavHost` with `NavDisplay` in activities | Activity files |
| 6.5 | Remove Navigation 2 dependencies | build files |
| 6.6 | Remove nav graph XML files | `res/navigation/*.xml` |

*Commit: `app: migrate from navigation 2 to navigation 3`*

---

## Phase 7 — App Module: Full Compose, Remove XML + ViewBinding

| Step | Change | Files |
|------|--------|-------|
| 7.1 | Replace `AndroidView` RecyclerViews with LazyColumn in Compose screens | `app/.../ui/screen/MainScreen.kt`, `CatalogScreen.kt`, `QuizResultScreen.kt` |
| 7.2 | Remove adapter classes (RecyclerView adapters, paging DataSource) | `app/.../adapter/` |
| 7.3 | Remove item view XML layouts | `res/layout/view_*.xml`, `view_spinner_*.xml` |
| 7.4 | Convert activities to full Compose (remove activity XML layouts) | Activity files, remove `res/layout/activity_*.xml` |
| 7.5 | Remove `MenuBottomNavigationView` → Compose `NavigationBar` | Remove from activities |
| 7.6 | Remove SettingFragment (PreferenceFragmentCompat) → Compose settings | `setting_fragment` |
| 7.7 | Remove ViewBinding from build | `app/build.gradle.kts` |
| 7.8 | Remove remaining utility files: `view.kt`, `AnimationUtil.kt`, unused resources | `app/.../util/` |

*Commit: `app: full compose, remove remaining xml and viewbinding`*

---

## Phase 8 — Cleanup

| Step | Change | Files |
|------|--------|-------|
| 8.1 | Remove unused resources (anim, drawable, xml/preferences, menu if unused) | `res/` |
| 8.2 | Remove `dependencies.gradle` (legacy file) | `dependencies.gradle` |
| 8.3 | Remove kapt plugin and references from all build files | `build.gradle.kts` files |
| 8.4 | Final build verification | `./gradlew :app:assembleDebug` |

*Commit: `cleanup: remove legacy files, final build verification`*

---

## Dependency Versions

| Library | Version | Notes |
|---------|---------|-------|
| Kotlin | 2.1.20 | Keep current |
| AGP | 8.13.1 | Keep current |
| Gradle | 8.13 | Keep current |
| Compose BOM | 2025.01.01 | Keep current |
| **Navigation 3** | **1.0.0** | New |
| **Koin** | **4.2.1** (via BOM) | New — replaces Dagger |
| **Kotlinx Serialization** | **1.8.1** (plugin 2.1.20) | New — replaces Gson |
| **Kotlinx Coroutines** | **1.10.2** | New — replaces RxJava |
| **Retrofit** | **2.12.0** | Upgrade from 2.5.0 |
| **OkHttp** | **4.12.0** | Upgrade from 3.11.0 |
| **Retrofit kotlinx-serialization** | **2.12.0** | New |
| Lifecycle ViewModel Compose | 2.8.x | Keep / bump |
| Navigation 2 | — | Remove |
| Dagger | — | Remove |
| Gson | — | Remove |
| RxJava / RxAndroid / RxKotlin | — | Remove |
| Paging 2 | — | Remove |
