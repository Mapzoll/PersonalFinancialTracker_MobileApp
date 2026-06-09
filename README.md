# Personal Financial Tracker Mobile App

## Table of Contents
1.  [About](#about)
2.  [Features](#features)
3.  [Installation](#installation)
4.  [Requirements](#requirements)
5.  [Building the Project](#building-the-project)
6.  [Dependencies](#dependencies)
7.  [Build Plugins](#build-plugins)
8.  [Technology Stack](#technology-stack)

## About

Personal Financial Tracker is a modern Android mobile application built with **Jetpack Compose** and **Kotlin**. This app helps users manage their personal finances efficiently by tracking income, expenses, and financial goals. With Firebase integration for cloud synchronization and Room Database for local storage, users can securely manage their financial data across multiple devices.

### Key Highlights:
-  Modern UI built with Jetpack Compose
-  Secure authentication with Firebase & 6-PIN Authentication
-  Real-time cloud sync with Firebase
-  Camera integration for receipt capture
- 📍 Location-based expense tracking
- 📊 Comprehensive financial analytics
- 🔔 Crash reporting for app stability

## Features

- ✅ User Authentication (Firebase Auth + 6-PIN Authentication)
- ✅ Track Income & Expenses
- ✅ Categorize Transactions
- ✅ View Financial Analytics
- ✅ Capture Receipt Photos
- ✅ Location-based Expense Tracking
- ✅ Cloud Backup & Sync
- ✅ Local Database Caching
- ✅ Real-time Notifications


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
4. Download `google-services.json`
5. Place it in the `app/` directory
6. Enable required Firebase services:
   - Authentication
   - Firestore Database
   - Realtime Database
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
- **Permissions Required**:
  - Camera
  - Location
  - Storage

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

---

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
- 6-PIN Authentication

### Media & Location
- Camera API (Core, Camera2, Lifecycle, View)
- Play Services Location (GPS)
- Coil (Image loading and caching)

### Architecture & State Management
- Lifecycle ViewModel
- Coroutines (async operations)
- Navigation Fragment/UI


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

---

## License

This project is licensed under the MIT License.

## Contact

For questions or support, please open an issue on the [GitHub repository](https://github.com/Mapzoll/PersonalFinancialTracker_MobileApp).
