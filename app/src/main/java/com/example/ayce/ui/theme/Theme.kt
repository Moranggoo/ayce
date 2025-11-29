package com.example.ayce.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val LightColorScheme = lightColorScheme(
    primary = MintPrimary,
    onPrimary = Color.White,

    secondary = PastelBlue,
    onSecondary = Color.White,

    background = SurfaceLight,
    onBackground = Color.Black,

    surface = SurfaceLight,
    onSurface = Color.Black,

    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = Color.DarkGray,

    outline = OutlineLight,

    error = ErrorRed,
    onError = Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = MintPrimaryLight,
    onPrimary = Color.Black,

    secondary = PastelBlue,
    onSecondary = Color.Black,

    background = Color(0xFF121212),
    onBackground = Color.White,

    surface = Color(0xFF1E1E1E),
    onSurface = Color.White,

    surfaceVariant = Color(0xFF2C2C2C),
    onSurfaceVariant = Color.LightGray,

    outline = Color(0xFF444444),

    error = ErrorRed,
    onError = Color.Black
)

@Composable
fun AyceTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}