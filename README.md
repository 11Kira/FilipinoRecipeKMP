<h1>FilipinoRecipeKMP (Kotlin Multiplatform Client)</h1>
<p>The modern architectural evolution of the Filipino Recipe platform. This project leverages Kotlin Multiplatform (KMP) to share 100% of the core business logic, data models, database structures, networking infrastructure, and authentication workflows between Android and iOS targets, while maintaining a unified declarative UI engine.</p>

<h2>🛠️ Tech Stack &amp; Cross-Platform Architecture</h2>
<ul>
  <li><strong>Core Sharing (<code>commonMain</code>):</strong> Kotlin Multiplatform (KMP)</li>
  <li><strong>UI Layer:</strong> Compose Multiplatform / Jetpack Compose</li>
  <li><strong>Local Storage (Offline-First):</strong> Room KMP Database (Multiplatform database abstraction layer engineered in shared code)</li>
  <li><strong>Media Asset Pipeline:</strong> Firebase Cloud Storage (Cross-platform cloud resource hosting)</li>
  <li><strong>Networking:</strong> Ktor HTTP Client (Engineered for multiplatform asynchronous non-blocking I/O)</li>
  <li><strong>Local Settings &amp; Credentials:</strong> Multiplatform Settings (<code>com.russhwolf:multiplatform-settings</code>)</li>
  <li><strong>Dependency Injection:</strong> Koin (Shared architecture multiplatform injection framework)</li>
  <li><strong>Reactive Core:</strong> Coroutines + StateFlow (State management) + SharedFlow (One-shot UI Events)</li>
</ul>

<h2>🏗️ Architectural Deep Dive</h2>
<ul>
  <li><strong>Shared Database Engine (Room KMP):</strong> Maximizes code sharing by establishing an offline-first cache right in the <code>commonMain</code> layer using Room for KMP. Defines shared entities, schemas, and Data Access Objects (DAOs) globally, while resolving target-specific SQLite compilation drivers dynamically in the platform source sets.</li>
  <li><strong>Advanced Session Management:</strong> Implements an automated token synchronization mechanism via a custom Ktor Bearer Authentication provider. Features programmatic in-memory token cache purging (<code>clearAuthTokens()</code>) upon logout to prevent multi-session credential leakage.</li>
  <li><strong>Firebase Storage Serialization:</strong> Abstracts dynamic resource location resolution through clean domain models, feeding remote image pathways seamlessly into Compose Multiplatform layouts with optimized rendering and memory patterns.</li>
  <li><strong>Declarative UI Routing State Engine:</strong> Manages complex, multi-screen feature lifecycles (such as a multi-step secure entry wizard) utilizing sealed state interfaces to entirely eliminate navigation state leaks and UI over-recomposition.</li>
  <li><strong>Unified Maintenance Lifecycle:</strong> Modifying backend communication data objects, executing database migrations, or updating repository validation rules automatically cascades changes across both target mobile operating systems simultaneously.</li>
</ul>
