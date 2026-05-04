package com.example.smartroute

import androidx.lifecycle.lifecycleScope
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.example.smartroute.ui.alerts.AlertBanner
import com.example.smartroute.ui.home.HomeScreen
import com.example.smartroute.ui.recent.RecentRoutesScreen
import com.example.smartroute.ui.settings.SettingsScreen
import com.example.smartroute.ui.theme.SmartRouteTheme
import com.example.smartroute.viewmodel.AlertViewModel
import com.example.smartroute.viewmodel.NavigationViewModel
import com.example.smartroute.viewmodel.RecentRoutesViewModel
import com.example.smartroute.viewmodel.SettingsViewModel
import kotlinx.coroutines.launch

private enum class AppScreen {
    HOME,
    RECENT_ROUTES,
    SETTINGS
}
class MainActivity : ComponentActivity() {

    private val alertViewModel: AlertViewModel by viewModels()
    private val navigationViewModel: NavigationViewModel by viewModels()
    private val settingsViewModel: SettingsViewModel by viewModels()
    private val recentRoutesViewModel: RecentRoutesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            // Reset theme to default if APK was freshly installed/updated
            settingsViewModel.resetThemeIfApkUpdated()

            // Clear recent routes if APK was updated
            recentRoutesViewModel.clearIfApkUpdated()

            setContent {
                val navUiState by navigationViewModel.uiState.collectAsState()
                val settingsState by settingsViewModel.settings.collectAsState()
                var currentScreen by rememberSaveable { mutableStateOf(AppScreen.HOME) }

                LaunchedEffect(navUiState.isNavigating) {
                    if (navUiState.isNavigating) {
                        alertViewModel.startListening()
                    } else {
                        alertViewModel.stopListening()
                    }
                }

                LaunchedEffect(navUiState.isCompleted) {
                    if (navUiState.isCompleted) {
                        val route = navigationViewModel.routeState.value
                        recentRoutesViewModel.addRecentRoute(
                            routeName = route.name,
                            start = route.start,
                            destination = route.destination,
                            durationMin = route.etaMinutes
                        )
                    }
                }

                SmartRouteTheme(themeMode = settingsState.themeMode) {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        Scaffold(
                            topBar = { AlertBanner(alertViewModel) }
                        ) { innerPadding ->
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding)
                            ) {
                                when (currentScreen) {
                                    AppScreen.HOME -> HomeScreen(
                                        navigationViewModel = navigationViewModel,
                                        onOpenRecentRoutes = { currentScreen = AppScreen.RECENT_ROUTES },
                                        onOpenSettings = { currentScreen = AppScreen.SETTINGS }
                                    )

                                    AppScreen.RECENT_ROUTES -> RecentRoutesScreen(
                                        viewModel = recentRoutesViewModel,
                                        onBack = { currentScreen = AppScreen.HOME }
                                    )

                                    AppScreen.SETTINGS -> SettingsScreen(
                                        viewModel = settingsViewModel,
                                        onBack = { currentScreen = AppScreen.HOME }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}

