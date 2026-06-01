package com.example.projectmap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.projectmap.ui.analytics.AnalyticsScreen
import com.example.projectmap.ui.auth.LoginScreen
import com.example.projectmap.ui.auth.PinVerifyScreen
import com.example.projectmap.ui.auth.RegisterScreen
import com.example.projectmap.ui.dashboard.AddTransactionScreen
import com.example.projectmap.ui.dashboard.AllTransactionsScreen
import com.example.projectmap.ui.dashboard.DashboardScreen
import com.example.projectmap.ui.settings.SettingsScreen
import com.example.projectmap.ui.theme.ProjectMAPTheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val systemTheme = isSystemInDarkTheme()
            var isDarkMode by remember { mutableStateOf(systemTheme) }

            ProjectMAPTheme(darkTheme = isDarkMode) {
                val navController = rememberNavController()
                val currentUser = remember { FirebaseAuth.getInstance().currentUser }
                val startDestination = if (currentUser != null) "dashboard" else "login"

                NavHost(navController = navController, startDestination = startDestination) {

                    composable("login") {
                        LoginScreen(
                            onLoginSuccess = { navController.navigate("dashboard") { popUpTo("login") { inclusive = true } } },
                            onNavigateToRegister = { navController.navigate("register") },
                        )
                    }

                    composable("register") {
                        RegisterScreen(
                            onRegisterSuccess = { navController.popBackStack() },
                            onNavigateToLogin = { navController.popBackStack() }
                        )
                    }

                    composable("dashboard") {
                        DashboardScreen(
                            isDarkMode = isDarkMode,
                            onThemeToggle = { isDarkMode = !isDarkMode },
                            onNavigateToAddTransaction = { navController.navigate("pin_verify") },
                            onNavigateToAllTransactions = { navController.navigate("all_transactions") },
                            onNavigateToAnalytics = { navController.navigate("analytics") { popUpTo(0) { inclusive = true } } },
                            onNavigateToSettings = { navController.navigate("settings") { popUpTo(0) { inclusive = true } } }
                        )
                    }

                    composable("pin_verify") {
                        PinVerifyScreen(
                            onPinVerified = {
                                navController.navigate("add_transaction") {
                                    popUpTo("pin_verify") { inclusive = true }
                                }
                            },
                            onLogout = { navController.popBackStack() }
                        )
                    }

                    composable("all_transactions") {
                        AllTransactionsScreen(
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    composable("analytics") {
                        AnalyticsScreen(
                            isDarkMode = isDarkMode,
                            onThemeToggle = { isDarkMode = !isDarkMode },
                            onNavigateToDashboard = { navController.navigate("dashboard") { popUpTo(0) { inclusive = true } } },
                            onNavigateToSettings = { navController.navigate("settings") { popUpTo(0) { inclusive = true } } }
                        )
                    }

                    composable("settings") {
                        SettingsScreen(
                            isDarkMode = isDarkMode,
                            onThemeToggle = { isDarkMode = !isDarkMode },
                            onNavigateToDashboard = { navController.navigate("dashboard") { popUpTo(0) { inclusive = true } } },
                            onNavigateToAnalytics = { navController.navigate("analytics") { popUpTo(0) { inclusive = true } } },
                            onLogoutSuccess = { navController.navigate("login") { popUpTo(0) { inclusive = true } } }
                        )
                    }

                    composable("add_transaction") {
                        AddTransactionScreen(
                            onNavigateBack = { navController.popBackStack() },
                            onLogout = {
                                FirebaseAuth.getInstance().signOut()
                                navController.navigate("login") { popUpTo(0) { inclusive = true } }
                            }
                        )
                    }
                }
            }
        }
    }
}