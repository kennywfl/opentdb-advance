package com.opentrivia.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = Blue,
    onPrimary = White,
    primaryContainer = Blue,
    secondary = Amber,
    onSecondary = White,
    background = White,
    surface = White,
    error = Red,
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkGrey,
    onPrimary = White,
    primaryContainer = DarkGrey,
    secondary = Purple,
    onSecondary = White,
    background = NearBlack,
    surface = DarkGrey,
    error = Red,
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = AppTypography,
        content = content
    )
}
