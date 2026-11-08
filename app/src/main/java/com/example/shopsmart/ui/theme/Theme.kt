package com.example.shopsmart.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = ColorWarmAmber,         // #D97706
    onPrimary = SurfaceWhite,
    primaryContainer = ColorSoftPeach,  // #F4D6B0
    onPrimaryContainer = ColorNavyDeep,
    secondary = ColorSkyCyan,         // #5BC0DE
    onSecondary = ColorNavyDeep,
    secondaryContainer = ColorSkyCyan.copy(alpha = 0.2f),
    onSecondaryContainer = ColorNavyDeep,
    tertiary = ColorSteelBlue,        // #3A506B
    onTertiary = SurfaceWhite,
    background = Color(0xFFF8F9FA),
    onBackground = ColorNavyDeep,
    surface = SurfaceWhite,
    onSurface = ColorNavyDeep,
    surfaceVariant = ColorSoftPeach.copy(alpha = 0.3f),
    onSurfaceVariant = ColorNavyDeep
)

private val DarkColorScheme = darkColorScheme(
    primary = ColorWarmAmber,         // #D97706
    onPrimary = SurfaceWhite,
    primaryContainer = ColorDarkSlate,  // #1C2541
    onPrimaryContainer = SurfaceWhite,
    secondary = ColorSkyCyan,         // #5BC0DE
    onSecondary = ColorNavyDeep,
    tertiary = ColorSteelBlue,        // #3A506B
    onTertiary = SurfaceWhite,
    background = ColorNavyDeep,       // #0B132B
    onBackground = SurfaceWhite,
    surface = ColorDarkSlate,         // #1C2541
    onSurface = SurfaceWhite,
    surfaceVariant = ColorSteelBlue,  // #3A506B
    onSurfaceVariant = SurfaceWhite
)

@Composable
fun ShopSimpleTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
