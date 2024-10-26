package com.hamidrezabashiri.calendar.presentation.screens.settings

import com.hamidrezabashiri.calendar.util.UiEffect
import com.hamidrezabashiri.calendar.util.UiIntent
import com.hamidrezabashiri.calendar.util.UiState

object SettingContract {
    data class State(
        val isDarkTheme: Boolean = false,
        val appLanguage : String = "en",
        val isLoading: Boolean = false,
        val error: String? = null
    ) : UiState

    sealed interface Intent : UiIntent {
        data class ChangeTheme(val isDarkTheme: Boolean) : Intent
        data class ChangeLanguage(val language: String) : Intent
    }

    sealed interface Effect : UiEffect {
        data class ShowError(val message: String) : Effect
        data class ShowToast(val message: String) : Effect
//        data class NavigateToEventDetail(val eventId: String) : Effect
//        data object ShowEventAddedSuccess : Effect
    }
}