package com.example.shopsmart.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = ShopRed,
    onPrimary = SurfaceWhite,
    primaryContainer = ShopButtercream,
    onPrimaryContainer = TextDark,
    secondary = ShopBlue,
    onSecondary = SurfaceWhite,
    secondaryContainer = ShopBlue.copy(alpha = 0.15f),
    onSecondaryContainer = ShopBlue,
    tertiary = ShopButtercream,
    onTertiary = TextDark,
    background = ShopButtercreamLight,
    onBackground = TextDark,
    surface = SurfaceWhite,
    onSurface = TextDark,
    surfaceVariant = ShopButtercream,
    onSurfaceVariant = TextDark
)

private val DarkColorScheme = darkColorScheme(
    primary = ShopRed,
    onPrimary = SurfaceWhite,
    primaryContainer = ShopRed.copy(alpha = 0.25f),
    onPrimaryContainer = SurfaceWhite,
    secondary = ShopBlue,
    onSecondary = SurfaceWhite,
    tertiary = ShopButtercream,
    onTertiary = TextDark,
    background = PrimaryBlack,
    onBackground = SurfaceWhite,
    surface = Color(0xFF1E1E1E),
    onSurface = SurfaceWhite,
    surfaceVariant = Color(0xFF2C2C2E),
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
