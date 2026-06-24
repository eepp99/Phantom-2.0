package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val PhantomColorScheme = darkColorScheme(
    primary = PhantomPurple,
    onPrimary = PhantomBg,
    primaryContainer = PhantomSurfaceHigh,
    onPrimaryContainer = PhantomText,
    secondary = SolanaTeal,
    onSecondary = PhantomBg,
    secondaryContainer = PhantomSurface,
    onSecondaryContainer = SolanaTeal,
    tertiary = PhantomPurpleBright,
    onTertiary = Color.White,
    background = PhantomBg,
    onBackground = PhantomText,
    surface = PhantomSurface,
    onSurface = PhantomText,
    surfaceVariant = PhantomSurfaceHigh,
    onSurfaceVariant = PhantomTextSecondary,
    outline = PhantomBorder,
    error = PhantomRed,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = PhantomColorScheme,
        typography = Typography,
        content = content
    )
}
