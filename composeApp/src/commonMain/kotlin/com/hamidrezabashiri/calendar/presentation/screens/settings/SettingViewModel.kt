package com.hamidrezabashiri.calendar.presentation.screens.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.hamidrezabashiri.calendar.presentation.screens.base.BaseViewModel
import kotlinx.coroutines.launch

class SettingViewModel : BaseViewModel<SettingContract.State, SettingContract.Intent, SettingContract.Effect>() {

    // Use Compose state for internal state management
    private var currentTheme by mutableStateOf(SettingContract.Theme.LIGHT)
    private var currentLanguage by mutableStateOf(SettingContract.Language.ENGLISH)
    private var isLoading by mutableStateOf(false)
    private var error by mutableStateOf<String?>(null)

    init {
        println("SettingViewModel initialized")
        loadInitialSettings()
    }

    override fun getInitialState() = SettingContract.State(
        currentTheme = SettingContract.Theme.LIGHT,
        currentLanguage = SettingContract.Language.ENGLISH,
        isLoading = true,
        error = null
    )

    @Composable
    override fun produceUiState(): SettingContract.State {
        return SettingContract.State(
            currentTheme = currentTheme,
            currentLanguage = currentLanguage,
            isLoading = isLoading,
            error = error
        )
    }

    private fun loadInitialSettings() {
        viewModelScope.launch {
            try {
                isLoading = true
                // TODO: Load settings from local storage or preferences
                // For now, using default values
                currentTheme = SettingContract.Theme.LIGHT
                currentLanguage = SettingContract.Language.ENGLISH
                isLoading = false
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    override fun handleIntent(intent: SettingContract.Intent) {
        viewModelScope.launch {
            when (intent) {
                is SettingContract.Intent.ChangeTheme -> handleThemeChange(intent.theme)
                is SettingContract.Intent.ChangeLanguage -> handleLanguageChange(intent.language)
            }
        }
    }

    private fun handleThemeChange(newTheme: SettingContract.Theme) {
        try {
            currentTheme = newTheme
            // TODO: Save theme preference
            emitEffect(SettingContract.Effect.ShowToast("Theme updated to ${newTheme.name}"))
        } catch (e: Exception) {
            handleError(e)
        }
    }

    private fun handleLanguageChange(newLanguage: SettingContract.Language) {
        try {
            currentLanguage = newLanguage
            // TODO: Save language preference
            emitEffect(SettingContract.Effect.ShowToast("Language updated to ${newLanguage.name}"))
        } catch (e: Exception) {
            handleError(e)
        }
    }

    private fun handleError(e: Exception) {
        error = e.message
        isLoading = false
        emitEffect(SettingContract.Effect.ShowError(e.message ?: "An error occurred"))
    }
}