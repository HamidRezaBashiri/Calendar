package com.hamidrezabashiri.calendar.presentation.navigation

import com.hamidrezabashiri.calendar.util.UiEffect

sealed class MainNavigationEffect : UiEffect {
    data class NavigateToAddEvent(val date: String? = null) : MainNavigationEffect()
    object NavigateBack : MainNavigationEffect()
}