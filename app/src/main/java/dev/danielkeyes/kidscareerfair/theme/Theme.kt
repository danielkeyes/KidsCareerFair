package dev.danielkeyes.kidscareerfair.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Not going to have light or dark theme for this app
private val DefaultColorScheme = darkColorScheme(
    primary = Color(0xFF40E0D0),
    secondary = Color(0xFFFFEB3B),
    tertiary =Color(0xFFF14411),
    background = Color(0xFF6495ed),
    surface = Color(0xFF6495ED),
    onPrimary = Color(0xFF2E7D32),
    onSecondary = Color(0xFF26A69A),
    onTertiary = Color(0xFF26A69A),
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
)

@Composable
fun KidsCareerFairTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = DefaultColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}