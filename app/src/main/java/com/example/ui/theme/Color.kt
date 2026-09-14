package com.example.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// CompositionLocal to track if Dark Mode is active
val LocalIsDarkTheme = staticCompositionLocalOf { false }

// Raw Color Palettes: Light and Dark

// Rose Gold Brand
val LightRoseGoldPrimary = Color(0xFF9E4358)
val DarkRoseGoldPrimary = Color(0xFFE27B91) // Elevated brightness for dark canvas

val LightRoseGoldSecondary = Color(0xFFB85D72)
val DarkRoseGoldSecondary = Color(0xFFD67B90)

val LightRoseGoldDark = Color(0xFF702636)
val DarkRoseGoldDark = Color(0xFFFCE7EC)

val LightRoseGoldTint = Color(0xFFFDECEF)
val DarkRoseGoldTint = Color(0xFF381F26)

// Champagne Gold Brand
val LightChampagneGold = Color(0xFFB8860B)
val DarkChampagneGold = Color(0xFFE5B842)

val LightChampagneGoldTint = Color(0xFFFEF9E7)
val DarkChampagneGoldTint = Color(0xFF352B17)

val LightChampagneGoldDark = Color(0xFF8C6505)
val DarkChampagneGoldDark = Color(0xFFFDE68A)

// Canvas & Surfaces
val LightCanvasBackground = Color(0xFFF6F7F9)
val DarkCanvasBackground = Color(0xFF101216)

val LightCardSurface = Color(0xFFFFFFFF)
val DarkCardSurface = Color(0xFF1A1D23)

val LightCardSurfaceVariant = Color(0xFFF9FAFB)
val DarkCardSurfaceVariant = Color(0xFF22262E)

val LightCardBorder = Color(0xFFE2E8F0)
val DarkCardBorder = Color(0xFF2F3540)

// Typography
val LightTextPrimary = Color(0xFF18181B)
val DarkTextPrimary = Color(0xFFF8FAFC)

val LightTextSecondary = Color(0xFF475569)
val DarkTextSecondary = Color(0xFF94A3B8)

val LightTextMuted = Color(0xFF94A3B8)
val DarkTextMuted = Color(0xFF64748B)

// Status & Semantic
val LightSuccessGreen = Color(0xFF15803D)
val DarkSuccessGreen = Color(0xFF22C55E)

val LightSuccessGreenTint = Color(0xFFDCFCE7)
val DarkSuccessGreenTint = Color(0xFF163824)

val LightWarningOrange = Color(0xFFD97706)
val DarkWarningOrange = Color(0xFFFBBF24)

val LightWarningOrangeTint = Color(0xFFFEF3C7)
val DarkWarningOrangeTint = Color(0xFF382B14)

val LightDangerRed = Color(0xFFDC2626)
val DarkDangerRed = Color(0xFFF87171)

val LightDangerRedTint = Color(0xFFFEE2E2)
val DarkDangerRedTint = Color(0xFF3B1A1E)

val InfoBlue = Color(0xFF2563EB)
val InfoBlueLight = Color(0xFFDBEAFE)

// Dynamic Composable Color Accessors (Seamless Dark/Light Mode)
val RoseGoldPrimary: Color
    @Composable get() = if (LocalIsDarkTheme.current) DarkRoseGoldPrimary else LightRoseGoldPrimary

val RoseGoldSecondary: Color
    @Composable get() = if (LocalIsDarkTheme.current) DarkRoseGoldSecondary else LightRoseGoldSecondary

val RoseGoldDark: Color
    @Composable get() = if (LocalIsDarkTheme.current) DarkRoseGoldDark else LightRoseGoldDark

val RoseGoldLight: Color
    @Composable get() = if (LocalIsDarkTheme.current) DarkRoseGoldTint else LightRoseGoldTint

val ChampagneGold: Color
    @Composable get() = if (LocalIsDarkTheme.current) DarkChampagneGold else LightChampagneGold

val ChampagneGoldLight: Color
    @Composable get() = if (LocalIsDarkTheme.current) DarkChampagneGoldTint else LightChampagneGoldTint

val ChampagneGoldDark: Color
    @Composable get() = if (LocalIsDarkTheme.current) DarkChampagneGoldDark else LightChampagneGoldDark

val CanvasBackground: Color
    @Composable get() = if (LocalIsDarkTheme.current) DarkCanvasBackground else LightCanvasBackground

val CardSurface: Color
    @Composable get() = if (LocalIsDarkTheme.current) DarkCardSurface else LightCardSurface

val CardSurfaceVariant: Color
    @Composable get() = if (LocalIsDarkTheme.current) DarkCardSurfaceVariant else LightCardSurfaceVariant

val CardBorder: Color
    @Composable get() = if (LocalIsDarkTheme.current) DarkCardBorder else LightCardBorder

val CardBorderFocused: Color
    @Composable get() = RoseGoldPrimary

val TextPrimary: Color
    @Composable get() = if (LocalIsDarkTheme.current) DarkTextPrimary else LightTextPrimary

val TextSecondary: Color
    @Composable get() = if (LocalIsDarkTheme.current) DarkTextSecondary else LightTextSecondary

val TextMuted: Color
    @Composable get() = if (LocalIsDarkTheme.current) DarkTextMuted else LightTextMuted

val SuccessGreen: Color
    @Composable get() = if (LocalIsDarkTheme.current) DarkSuccessGreen else LightSuccessGreen

val SuccessGreenLight: Color
    @Composable get() = if (LocalIsDarkTheme.current) DarkSuccessGreenTint else LightSuccessGreenTint

val WarningOrange: Color
    @Composable get() = if (LocalIsDarkTheme.current) DarkWarningOrange else LightWarningOrange

val WarningOrangeLight: Color
    @Composable get() = if (LocalIsDarkTheme.current) DarkWarningOrangeTint else LightWarningOrangeTint

val DangerRed: Color
    @Composable get() = if (LocalIsDarkTheme.current) DarkDangerRed else LightDangerRed

val DangerRedLight: Color
    @Composable get() = if (LocalIsDarkTheme.current) DarkDangerRedTint else LightDangerRedTint

// Backward-compatibility aliases
val SoftBlushBackground: Color @Composable get() = CanvasBackground
val SoftBlushCard: Color @Composable get() = CardSurface
val SoftBlushCardVariant: Color @Composable get() = CardSurfaceVariant
val SoftBlushBorder: Color @Composable get() = CardBorder
val CharcoalText: Color @Composable get() = TextPrimary
val CharcoalMuted: Color @Composable get() = TextSecondary
val CharcoalLight: Color @Composable get() = TextMuted
