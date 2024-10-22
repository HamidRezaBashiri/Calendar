package com.hamidrezabashiri.calendar.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

// Define the light color scheme using AppColors
private val LightColorPalette = lightColors(
    primary = AppColors.primaryLight,
    primaryVariant = AppColors.primaryContainerLight,  // Equivalent to "Container" in Material 3
    onPrimary = AppColors.onPrimaryLight,
    secondary = AppColors.secondaryLight,
    onSecondary = AppColors.onSecondaryLight,
    error = AppColors.errorLight,
    onError = AppColors.onErrorLight,
    background = AppColors.backgroundLight,
    onBackground = AppColors.onBackgroundLight,
    surface = AppColors.surfaceLight,
    onSurface = AppColors.onSurfaceLight
)

// Define the dark color scheme using AppColors
private val DarkColorPalette = darkColors(
    primary = AppColors.primaryDark,
    primaryVariant = AppColors.primaryContainerDark,
    onPrimary = AppColors.onPrimaryDark,
    secondary = AppColors.secondaryDark,
    onSecondary = AppColors.onSecondaryDark,
    error = AppColors.errorDark,
    onError = AppColors.onErrorDark,
    background = AppColors.backgroundDark,
    onBackground = AppColors.onBackgroundDark,
    surface = AppColors.surfaceDark,
    onSurface = AppColors.onSurfaceDark
)

// AppTheme composable to toggle between light and dark mode
@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),  // Automatically switch based on system setting
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        content = content
    )
}