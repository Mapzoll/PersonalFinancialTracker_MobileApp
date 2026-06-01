package com.example.projectmap.ui.theme

import android.app.Activity
import android.view.View
import android.view.Window
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = NeoPrimary,
    background = NeoBackground,
    surface = NeoBackground,
    onPrimary = NeoText,
    onBackground = NeoText,
    onSurface = NeoText
)

private val DarkColorScheme = darkColorScheme(
    primary = NeoPrimary,
    background = NeoDarkBackground,
    surface = NeoDarkBackground,
    onPrimary = NeoBackground,
    onBackground = NeoBackground,
    onSurface = NeoBackground
)

@Suppress("DEPRECATION")
@Composable
fun ProjectMAPTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view: View = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window: Window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}