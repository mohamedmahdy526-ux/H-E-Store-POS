package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = RoseGoldPrimary,
    onPrimary = Color.White,
    primaryContainer = SoftBlushCardVariant,
    onPrimaryContainer = RoseGoldDark,
    secondary = ChampagneGold,
    onSecondary = CharcoalText,
    secondaryContainer = ChampagneGoldLight,
    onSecondaryContainer = ChampagneGoldDark,
    tertiary = RoseGoldSecondary,
    onTertiary = Color.White,
    background = SoftBlushBackground,
    onBackground = CharcoalText,
    surface = SoftBlushCard,
    onSurface = CharcoalText,
    surfaceVariant = SoftBlushCardVariant,
    onSurfaceVariant = CharcoalMuted,
    outline = SoftBlushBorder,
    error = DangerRed,
    onError = Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = RoseGoldLight,
    onPrimary = CharcoalText,
    primaryContainer = RoseGoldDark,
    onPrimaryContainer = SoftBlushBackground,
    secondary = ChampagneGold,
    onSecondary = CharcoalText,
    secondaryContainer = ChampagneGoldDark,
    onSecondaryContainer = ChampagneGoldLight,
    tertiary = RoseGoldSecondary,
    onTertiary = Color.White,
    background = Color(0xFF1E1719),
    onBackground = Color(0xFFF7ECED),
    surface = Color(0xFF281F22),
    onSurface = Color(0xFFF7ECED),
    surfaceVariant = Color(0xFF382C30),
    onSurfaceVariant = Color(0xFFD6C0C4),
    outline = Color(0xFF5A4449),
    error = Color(0xFFEF5350),
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Preserve brand Rose Gold & Champagne palette
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun HEStoreTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MyApplicationTheme(darkTheme = darkTheme, dynamicColor = dynamicColor, content = content)
}
