# FilipinoRecipeKMP (Kotlin Multiplatform Client)

The modern architectural evolution of the Filipino Recipe platform. This project leverages Kotlin Multiplatform (KMP) to share 100% of the core business logic, data models, networking infrastructure, and authentication workflows between Android and iOS targets, while maintaining a unified UI engine.

## 🛠️ Tech Stack & Cross-Platform Architecture
* **Core Sharing (`commonMain`):** Kotlin Multiplatform
* **UI Layer:** Compose Multiplatform / Jetpack Compose
* **Networking:** Ktor HTTP Client (Engineered for cross-platform asynchronous I/O)
* **Local Caching & Persistence:** Multiplatform Settings (`com.russhwolf:multiplatform-settings`)
* **Dependency Injection:** Koin (Shared architecture multiplatform injection)
* **Reactive Core:** Coroutines + StateFlow (State management) + SharedFlow (One-shot UI Events)

## 🏗️ Architectural Deep Dive
* **Advanced Session Management:** Implements an automated token synchronization mechanism via a custom Ktor Bearer Authentication provider. Features programmatic in-memory token cache purging (`clearAuthTokens()`) upon logout to prevent multi-session credential leakage.
* **Declarative UI Routing State Engine:** Manages complex, multi-screen feature lifecycles (such as the 3-step secure password reset wizard) utilizing sealed state interfaces (`ForgotPasswordStep`) to eliminate state-leaks and UI over-recomposition.
* **Unified Maintenance Lifecycle:** Modifying backend communication data objects or updating repository validation rules automatically cascades changes across both target mobile operating systems simultaneously.
