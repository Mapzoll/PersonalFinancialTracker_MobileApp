# Personal Financial Tracker Mobile App

## Table of Contents
- [About](#about)
- [Features](#features)
- [Installation](#installation)
- [Requirements](#requirements)
- [Building the Project](#building-the-project)
- [Dependencies](#dependencies)
- [Build Plugins](#build-plugins)
- [Technology Stack](#technology-stack)

---

## About

Personal Financial Tracker is a modern Android mobile application built with **Jetpack Compose** and **Kotlin**. This app helps users manage their personal finances efficiently by tracking income and expenses in real-time. With Firebase integration for cloud synchronization, users can securely manage their financial data with 6-digit PIN authentication.

### Key Highlights:
- Modern UI built with Jetpack Compose
- Secure authentication with 6-digit PIN
- Real-time cloud sync with Firebase
- Camera integration for receipt capture
- Location-based expense tracking
- Comprehensive financial analytics
- Crash reporting for app stability

---

## Features

- ✅ User Authentication (Firebase Auth + 6-Digit PIN)
- ✅ Track Income & Expenses
- ✅ Categorize Transactions
- ✅ View Financial Analytics
- ✅ Capture Receipt Photos
- ✅ Location-based Expense Tracking
- ✅ Real-time Cloud Sync
- ✅ Expense Reports & Insights
- ✅ Real-time Notifications

---

## Installation

### Prerequisites

Before you begin, ensure you have the following installed:

1. **Android Studio** (Latest version)
   - Download from: https://developer.android.com/studio
   
2. **JDK 11 or Higher**
   - Download from: https://www.oracle.com/java/technologies/downloads/
   
3. **Android SDK**
   - Minimum SDK: Android 7.0 (API 24)
   - Target SDK: Android 13 (API 37)
   - Install via Android Studio's SDK Manager

4. **Git**
   - Download from: https://git-scm.com/

### Steps to Install

```bash
# 1. Clone the repository
git clone https://github.com/Mapzoll/PersonalFinancialTracker_MobileApp.git

# 2. Navigate to the project directory
cd PersonalFinancialTracker_MobileApp

# 3. Open the project in Android Studio
# File → Open → Select the project folder

# 4. Sync Gradle files
# Android Studio will automatically sync dependencies

# 5. Configure Firebase
# - Add your google-services.json file to the app/ directory
# - Download from Firebase Console: https://console.firebase.google.com/

# 6. Build the project
# Build → Make Project (Ctrl+F9 / Cmd+F9)

# 7. Run on the emulator or device
# Run → Run 'app' (Shift+F10 / Ctrl+R)
```

### Firebase Setup

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Create a new project
3. Add Android app to your Firebase project
4. Download `google-services.json.`
5. Place it in the `app/` directory
6. Enable required Firebase services:
   - Authentication
   - Firestore Database
   - Crashlytics

---

## Requirements

### System Requirements

- **Operating System**: Windows, macOS, or Linux
- **RAM**: 8 GB minimum (16 GB recommended)
- **Disk Space**: 10 GB for Android Studio + SDK

### Software Requirements

| Component | Version |
|-----------|---------|
| Android Studio | Latest (2024.x) |
| JDK | 11 or higher |
| Gradle | 8.0+ (auto-managed) |
| Kotlin | 1.9+ (auto-managed) |
| Android SDK | 37 (Target) |
| Minimum Android | 7.0 (API 24) |

### Device Requirements

- **Android Version**: 7.0 (API 24) or higher
- **RAM**: 2 GB minimum
- **Storage**: 100 MB free space
- **Internet**: Required for all features (cloud-based application)
- **Permissions Required**:
  - Camera (for receipt capture)
  - Location (for expense tracking)
  - Storage (for image caching)

---

## Building the Project

### Using Android Studio

1. Open Android Studio
2. Select "File" → "Open"
3. Navigate to the project folder
4. Wait for Gradle sync to complete
5. Click "Build" → "Make Project"
6. Run on an emulator or a connected device

### Using Command Line

```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Run tests
./gradlew test

# Clean build
./gradlew clean
```

---

## Dependencies used

All dependencies are managed through version catalogs in `gradle/libs.versions.toml` and direct implementations in `app/build.gradle.kts`.

### 1. UI & Compose Dependencies

```kotlin
dependencies {
    // Jetpack Compose Core
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    
    // Compose Icons
    implementation(libs.androidx.compose.material.icons.core)
    implementation(libs.androidx.compose.material.icons.extended)
    
    // Compose Adaptive
    implementation(libs.androidx.compose.adaptive)
    implementation(libs.androidx.compose.adaptive.layout)
    implementation(libs.androidx.compose.adaptive.navigation3)
    
    // Activity & Compose Integration
    implementation(libs.androidx.activity.compose)
    
    // Material Design
    implementation(libs.material)
    
    // ConstraintLayout
    implementation(libs.androidx.constraintlayout)
    
    // Accompanist (Permissions & more)
    implementation(libs.accompanist.permissions)
}
```

### 2. Navigation Dependencies

```kotlin
dependencies {
    // Navigation Compose
    implementation("androidx.navigation:navigation-compose:2.7.5")
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    
    // Navigation Fragment & UI
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    
    // ViewModel Navigation
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
}
```

### 3. Firebase Dependencies

```kotlin
dependencies {
    // Firebase BOM for version management
    implementation(platform("com.google.firebase:firebase-bom:34.13.0"))
    implementation(platform(libs.firebase.bom))
    
    // Firebase Core Services
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-crashlytics")
}
```

### 4. Database & Storage Dependencies

```kotlin
dependencies {
    // DataStore Preferences
    implementation(libs.androidx.datastore.preferences)
}
```

### 5. Networking Dependencies

```kotlin
dependencies {
    // Retrofit
    implementation(libs.retrofit)
    
    // OkHttp
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    
    // Moshi (JSON Serialization)
    implementation(libs.moshi.kotlin)
    implementation(libs.converter.moshi)
}
```

### 6. Lifecycle & Coroutines Dependencies

```kotlin
dependencies {
    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    
    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    
    // Serialization
    implementation(libs.kotlinx.serialization.core)
}
```

### 7. Camera & Media Dependencies

```kotlin
dependencies {
    // Camera
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    
    // Image Loading
    implementation(libs.coil.compose)
    
    // Location Services
    implementation(libs.play.services.location)
}
```

### 8. Security Dependencies

```kotlin
dependencies {
    // Dependency Injection
    implementation("javax.inject:javax.inject:1")
    
    // Core KTX
    implementation(libs.androidx.core.ktx)
}
```

### 9. RecyclerView Dependencies

```kotlin
dependencies {
    // RecyclerView
    implementation(libs.androidx.recyclerview)
}
```

### 10. Testing Dependencies

```kotlin
dependencies {
    // Unit Testing
    testImplementation(libs.junit)
    testImplementation(libs.androidx.junit)
    testImplementation(libs.androidx.core)
    testImplementation(libs.kotlinx.coroutines.test)
    
    // Android Testing
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.runner)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    
    // Debug Testing
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
```

### 11. Build & Compilation Dependencies

```kotlin
dependencies {
    // KSP - Kotlin Symbol Processing
    "ksp"(libs.androidx.room.compiler)
    "ksp"(libs.moshi.kotlin.codegen)
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
- ConstraintLayout
- Material Icons

### Database & Storage
- Firebase Firestore (Cloud database)
- DataStore Preferences

### Networking
- Retrofit (HTTP client)
- OkHttp (HTTP interceptor)
- Moshi (JSON serialization)
- Logging Interceptor

### Authentication & Security
- Firebase Authentication
- 6-Digit PIN Authentication
- Dependency Injection (javax.inject)

### Media & Location
- Camera API (Core, Camera2, Lifecycle, View)
- Play Services Location (GPS)
- Coil (Image loading and caching)

### Architecture & State Management
- Lifecycle ViewModel
- Coroutines (Core & Android)
- Navigation (Compose, Fragment, UI)
- Kotlin Serialization
- Core KTX

### Testing
- JUnit
- AndroidX Test (Core, JUnit)
- Espresso
- Compose UI Test
- Coroutines Test

### Build & Code Generation
- KSP (Kotlin Symbol Processing)
- Room Compiler
- Moshi Kotlin Codegen

---

## Important Notes

### Internet Requirement
This application requires an active internet connection for all features as it is a cloud-based application. All financial data is stored in Firebase Firestore and cannot be accessed offline.

### Authentication
The app uses a 6-digit PIN authentication method combined with Firebase Authentication for secure user access.

---

## Minimum Requirements

- **Minimum SDK**: 24 (Android 7.0)
- **Target SDK**: 37 (Android 13)
- **Java Compatibility**: JDK 11+
- **Test Runner**: AndroidJUnitRunner
- **Internet Connection**: Required

---

## License

This project is licensed under the MIT License.

## Contact

For questions or support, please open an issue on the [GitHub repository](https://github.com/Mapzoll/PersonalFinancialTracker_MobileApp).
