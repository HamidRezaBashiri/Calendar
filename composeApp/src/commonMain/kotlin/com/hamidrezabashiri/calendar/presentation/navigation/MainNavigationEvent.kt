package com.hamidrezabashiri.calendar.presentation.navigation

import com.hamidrezabashiri.calendar.util.UiEvent

sealed class MainNavigationEvent : UiEvent {
    data class TabSelected(val index: Int) : MainNavigationEvent()
}