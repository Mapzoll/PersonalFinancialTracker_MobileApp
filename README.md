## Dependencies used

### 1. RecyclerView

```kotlin
dependencies {
    implementation("androidx.recyclerview:recyclerview:1.4.0")
    implementation("androidx.recyclerview:recyclerview-selection:1.2.0")
}
```

### 2. Firebase

```kotlin
dependencies {
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-crashlytics")
}
```

---

## Build Plugins

The project uses the following Gradle plugins configured in `build.gradle.kts`:

```gradle
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.jetbrains.kotlin.plugin.serialization)
    id("com.google.gms.google-services") version "4.4.4"
    id("com.google.firebase.crashlytics") version "3.0.2"
}
```

**Plugin Details:**
- **android.application** - Android app development
- **kotlin.compose** - Jetpack Compose support for Kotlin
- **google.devtools.ksp** - Kotlin Symbol Processing for annotation processing
- **jetbrains.kotlin.plugin.serialization** - Kotlin serialization support
- **com.google.gms.google-services** - Google Services (Firebase integration)
- **com.google.firebase.crashlytics** - Firebase Crash Reporting

---

## Technology Stack

### UI & Compose
- Jetpack Compose (Modern Android UI framework)
- Material Design 3
- Compose Navigation
- Accompanist Permissions

### Database & Storage
- Room Database (Local data persistence)
- Firebase Firestore (Cloud database)
- Firebase Realtime Database
- DataStore Preferences

### Networking
- Retrofit (HTTP client)
- OkHttp (HTTP interceptor)
- Moshi (JSON serialization)

### Authentication & Security
- Firebase Authentication
- Biometric authentication (fingerprint)

### Media & Location
- Camera API (Core, Camera2, Lifecycle, View)
- Play Services Location (GPS)
- Coil (Image loading and caching)

### Architecture & State Management
- Lifecycle ViewModel
- Coroutines (async operations)
- Navigation Fragment/UI

### Testing
- JUnit
- AndroidX Test
- Espresso
- Compose UI Test

### Build & Code Generation
- KSP (Kotlin Symbol Processing)
- Room Compiler
- Moshi Kotlin Codegen

---

## Minimum Requirements

- **Minimum SDK**: 24 (Android 7.0)
- **Target SDK**: 37 (Android 13)
- **Java Compatibility**: JDK 11+
- **Test Runner**: AndroidJUnitRunner
