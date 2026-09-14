package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = LightRoseGoldPrimary,
    onPrimary = Color.White,
    primaryContainer = LightRoseGoldTint,
    onPrimaryContainer = LightRoseGoldDark,
    secondary = LightChampagneGold,
    onSecondary = LightTextPrimary,
    secondaryContainer = LightChampagneGoldTint,
    onSecondaryContainer = LightChampagneGoldDark,
    tertiary = LightRoseGoldSecondary,
    onTertiary = Color.White,
    background = LightCanvasBackground,
    onBackground = LightTextPrimary,
    surface = LightCardSurface,
    onSurface = LightTextPrimary,
    surfaceVariant = LightCardSurfaceVariant,
    onSurfaceVariant = LightTextSecondary,
    outline = LightCardBorder,
    error = LightDangerRed,
    onError = Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkRoseGoldPrimary,
    onPrimary = Color.White,
    primaryContainer = DarkRoseGoldTint,
    onPrimaryContainer = DarkRoseGoldDark,
    secondary = DarkChampagneGold,
    onSecondary = DarkTextPrimary,
    secondaryContainer = DarkChampagneGoldTint,
    onSecondaryContainer = DarkChampagneGoldDark,
    tertiary = DarkRoseGoldSecondary,
    onTertiary = Color.White,
    background = DarkCanvasBackground,
    onBackground = DarkTextPrimary,
    surface = DarkCardSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkCardSurfaceVariant,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkCardBorder,
    error = DarkDangerRed,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    CompositionLocalProvider(LocalIsDarkTheme provides darkTheme) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

@Composable
fun HEStoreTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MyApplicationTheme(darkTheme = darkTheme, dynamicColor = dynamicColor, content = content)
}
