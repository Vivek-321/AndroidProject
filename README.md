# SmartRoute – Android Automotive OS (AAOS)

## Overview
SmartRoute is a mock Android Automotive OS application built using **Kotlin**, **Jetpack Compose**, **MVVM**, **Repository pattern**, **StateFlow**, and **Preferences DataStore**.

The project simulates a lightweight in-car navigation experience with:
- Home screen with editable **Start** and **Destination**
- Mock **turn-by-turn navigation**
- **Road alert banner** shown only during active navigation
- **Recent Routes** persistence
- **Settings** persistence
- **Day / Night / System theme selection** from Settings
- **Graceful navigation resume** using saved navigation session state

> This project uses mocked data only. There is no real GPS, backend, map SDK, or internet dependency.

---

## Functional Scope

### 1. Home Screen
Displays:
- Route title
- Editable Start and Destination
- Quick destination buttons
- Distance and ETA cards
- Start Navigation button
- Recent Routes button
- Settings button

### 2. Navigation Screen
Displays:
- Current navigation instruction
- Distance to next turn
- Remaining route distance
- Remaining time
- Direction indicator
- Back button
- Destination reached state

### 3. Road Alerts
- Alerts are shown only while navigation is active
- Alert banner is non-intrusive and auto-dismisses
- Alerts are simulated from a background Flow

### 4. Recent Routes
Stores and displays the last completed routes:
- Route name
- Start and Destination
- Date/time
- Duration

Behavior:
- Keeps only the latest **5** routes
- Stored using **Preferences DataStore**
- Automatically cleared when a new APK install/update is detected

### 5. Settings
Includes:
- Voice Instructions (Enabled / Disabled)
- Alert Intensity (Low / Medium / High)
- Theme Mode (System / Light / Dark)
- App Version
- Android Automotive Version
- Build Type

Behavior:
- Stored using **Preferences DataStore**
- Persists across normal app restarts
- Theme resets to default behavior after fresh APK update/install if configured that way in repository logic

### 6. Theme Support
- App supports **Light**, **Dark**, and **System** theme modes
- Theme selection is controlled from the Settings screen
- Applied globally via `SmartRouteTheme`

### 7. Background / Resume Support
- Current navigation step is stored locally using a navigation session repository
- When the app returns from background, navigation can resume from the saved step index
- This is a demo-level resume implementation, not a foreground service

---

## Architecture
The project follows **MVVM + Repository** architecture.

### Layers
#### UI Layer (Jetpack Compose)
- `HomeScreen`
- `NavigationScreen`
- `RecentRoutesScreen`
- `SettingsScreen`
- `AlertBanner`

#### ViewModel Layer
- `NavigationViewModel`
- `AlertViewModel`
- `RecentRoutesViewModel`
- `SettingsViewModel`

#### Repository Layer
- `NavigationRepository`
- `AlertRepository`
- `RecentRoutesRepository`
- `SettingsRepository`
- `NavigationSessionRepository`

#### Persistence Layer
- `Preferences DataStore`
- Shared app-level DataStore instance via `AppDataStore.kt`

---

## Project Structure

com.example.smartroute
├── MainActivity.kt
├── data
│   ├── datastore
│   │   └── AppDataStore.kt
│   ├── model
│   │   ├── Route.kt
│   │   ├── NavStep.kt
│   │   ├── NavigationSession.kt
│   │   ├── RoadAlert.kt
│   │   ├── RecentRoute.kt
│   │   └── SettingsData.kt
│   └── repo
│       ├── NavigationRepository.kt
│       ├── NavigationSessionRepository.kt
│       ├── AlertRepository.kt
│       ├── RecentRoutesRepository.kt
│       └── SettingsRepository.kt
├── ui
│   ├── alerts
│   │   └── AlertBanner.kt
│   ├── home
│   │   └── HomeScreen.kt
│   ├── navigation
│   │   └── NavigationScreen.kt
│   ├── recent
│   │   └── RecentRoutesScreen.kt
│   ├── settings
│   │   └── SettingsScreen.kt
│   └── theme
│       ├── Color.kt
│       └── Theme.kt
└── viewmodel
    ├── AlertViewModel.kt
    ├── NavigationViewModel.kt
    ├── NavUiState.kt
    ├── RecentRoutesViewModel.kt
    └── SettingsViewModel.kt

---

## Important Flows

### App Startup Flow
1. `MainActivity` starts
2. Recent route cleanup / theme reset logic runs if required
3. Compose UI starts
4. `HomeScreen` is shown by default

### Home → Navigation Flow
1. User edits Start / Destination
2. User taps **Start Navigation**
3. `NavigationViewModel` updates the route state
4. `NavigationRepository` emits navigation steps
5. `NavigationScreen` updates automatically from `StateFlow`

### Alert Flow
1. `MainActivity` observes navigation state
2. When navigation starts, `AlertViewModel.startListening()` is triggered
3. `AlertRepository` emits mocked alerts
4. `AlertBanner` displays them and auto-dismisses
5. When navigation stops, alert listening stops

### Recent Routes Flow
1. Navigation completes
2. `MainActivity` adds the route to `RecentRoutesViewModel`
3. `RecentRoutesRepository` saves the route in DataStore as JSON
4. `RecentRoutesScreen` reads the list using `StateFlow`

### Settings Flow
1. `SettingsRepository` reads values from DataStore
2. `SettingsViewModel` exposes them as `SettingsData`
3. `SettingsScreen` shows current values
4. User changes a setting
5. ViewModel saves the new value using repository
6. UI refreshes automatically

### Background Resume Flow
1. While navigation is active, `NavigationViewModel` saves the current step index
2. Session is stored through `NavigationSessionRepository`
3. When app reopens, ViewModel restores the session if available
4. Navigation resumes from the saved step

---

## How to Run
1. Open project in Android Studio
2. Sync Gradle
3. Create or select an Android Automotive emulator
4. Run the `automotive` module
5. Test:
   - Home screen
   - Start navigation
   - Road alerts
   - Theme switching
   - Recent Routes
   - Settings persistence
   - Background/foreground resume

---

## Key Dependencies
```kotlin
dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    implementation(platform("androidx.compose:compose-bom:2024.06.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    implementation("androidx.datastore:datastore-preferences:1.2.1")
}

---

## Notes
- All data is mocked
- Architecture and state handling are the main focus
- No real maps, GPS, or online APIs are used
