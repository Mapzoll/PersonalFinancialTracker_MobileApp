# Project Plan

Project MAP: A comprehensive personal finance tracker. 
Key components to implement:
1. Firebase Integration: Real-time sync, Authentication (email/pass + 6-digit PIN), 2FA. (With local-only Demo Mode fallback).
2. Dashboard: Total Balance, Income vs Expense summary, Doughnut chart for category distribution.
3. Transaction Management: Income/Expense input, Category manager (customizable), NLP Indonesian parser (regex-based on-device or simple logic as requested).
4. Analytics: Savings Filter (Defense Ratio), Hull Leak Radar (Category bar charts), Daily Consumption Pulse (Line chart).
5. Document Export: PDF (PdfDocument) and Excel (CSV) export via FileProvider.
6. Push Notifications: Budget reminders.
7. Design: Material 3, Dark Mode, Edge-to-Edge, Adaptive App Icon.
8. Navigation: Jetpack Navigation following the provided flowcharts (Auth -> Dashboard -> Analytics/Settings).
9. Layout Folder: Create a 'layout' folder for XML-based design modification as requested (though the app will use Compose, provide XML layouts for reference or as requested).

## Project Brief

# Project Brief: Project MAP

Project MAP is a modern personal financial management application designed to provide users with a seamless and secure way to track expenses. By leveraging NLP for natural language entry and a vibrant Material Design 3 interface, it simplifies financial oversight while ensuring data is synchronized and secure.

## Features

- **Automated Expense Tracking with NLP:** Quick-log transactions using an Indonesian natural language parser that extracts amount, category, and date from simple notes.
- **Real-time Analytics Dashboard:** Interactive Material 3 charts and graphs that provide immediate insights into spending habits and budget status.
- **Secure Authentication:** Multi-layered security including 2FA and PIN-based access to protect sensitive financial information.
- **Firebase Real-time Sync & Export:** Automatic data synchronization across devices via Firebase, with the ability to export financial reports to PDF and Excel formats.
- **Demo Mode**: Ability to use the app locally without Firebase if configuration is missing.

## High-Level Technical Stack
- **Language:** Kotlin
- **UI Framework:** Jetpack Compose with Material Design 3 (Full Edge-to-Edge support)
- **Navigation:** Jetpack Navigation 3 (State-driven architecture)
- **Adaptive Layout:** Compose Material Adaptive library for optimized multi-device support
- **Concurrency:** Kotlin Coroutines & Flow
- **Backend/Sync:** Firebase Authentication & Firebase Real-time Database (with Local-only fallback)
- **Networking:** Retrofit & Moshi (for NLP API communication)
- **Image Loading:** Coil

## UI Design 
Neobrutalism UI


