package com.prototipo.alertacusco.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = AlertaBlue,
    onPrimary = Color.White,

    secondary = AlertaGreen,
    onSecondary = Color.White,

    tertiary = AlertaOrange,
    onTertiary = Color.White,

    background = LightBackground,
    onBackground = LightTextPrimary,

    surface = LightSurface,
    onSurface = LightTextPrimary,

    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = LightTextSecondary,

    primaryContainer = Color(0xFFE8F0FF),
    onPrimaryContainer = AlertaBlue,

    error = AlertaRed,
    onError = Color.White,

    outline = LightBorder
)

private val DarkColorScheme = darkColorScheme(
    primary = AlertaBlueDark,
    onPrimary = Color.White,

    secondary = AlertaGreen,
    onSecondary = Color.White,

    tertiary = AlertaOrange,
    onTertiary = Color.White,

    background = DarkBackground,
    onBackground = DarkTextPrimary,

    surface = DarkSurface,
    onSurface = DarkTextPrimary,

    surfaceVariant = DarkCard,
    onSurfaceVariant = DarkTextSecondary,

    primaryContainer = Color(0xFF123A66),
    onPrimaryContainer = Color.White,

    error = AlertaRed,
    onError = Color.White,

    outline = DarkBorder
)

@Composable
fun AlertacuscoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme: ColorScheme =
        if (darkTheme) {
            DarkColorScheme
        } else {
            LightColorScheme
        }

    val view = LocalView.current

    if (!view.isInEditMode) {
        val window = (LocalContext.current as Activity).window

        window.statusBarColor = colorScheme.background.toArgb()
        window.navigationBarColor = colorScheme.background.toArgb()

        WindowCompat.getInsetsController(
            window,
            view
        ).isAppearanceLightStatusBars = !darkTheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}