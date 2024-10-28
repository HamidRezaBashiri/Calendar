package com.hamidrezabashiri.calendar.presentation.screens.settings

import com.hamidrezabashiri.calendar.util.UiEffect
import com.hamidrezabashiri.calendar.util.UiIntent
import com.hamidrezabashiri.calendar.util.UiState


class SettingContract {
    data class State(
        val currentTheme: Theme = Theme.LIGHT,
        val currentLanguage: Language = Language.ENGLISH,
        val isLoading: Boolean = false,
        val error: String? = null
    ) : UiState

    sealed class Intent : UiIntent {
        data class ChangeTheme(val theme: Theme) : Intent()
        data class ChangeLanguage(val language: Language) : Intent()
    }

    sealed class Effect : UiEffect {
        data class ShowToast(val message: String) : Effect()
        data class ShowError(val message: String) : Effect()
    }

    enum class Theme {
        LIGHT, DARK, SYSTEM
    }

    enum class Language {
        ENGLISH, PERSIAN, ARABIC
    }
}
