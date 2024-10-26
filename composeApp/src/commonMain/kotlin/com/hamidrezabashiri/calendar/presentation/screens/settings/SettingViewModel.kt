package com.hamidrezabashiri.calendar.presentation.screens.settings

import com.hamidrezabashiri.calendar.presentation.screens.base.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingViewModel() :BaseViewModel<SettingContract.State, SettingContract.Intent, SettingContract.Effect>() {

    private val _state = MutableStateFlow(SettingContract.State())

    override val state = produceState(SettingContract.State()) {
        _state.value
    }

    override val effects = MutableSharedFlow<SettingContract.Effect>()

    init {
        println("SettingViewModel initialized") // Example log statement

        loadInitialSettings()
    }


    private fun loadInitialSettings() {
        viewModelScope.launch {
            try {
                // Load initial settings
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }


    override fun handleIntent(intent: SettingContract.Intent) {
        when(intent) {
            is SettingContract.Intent.ChangeTheme -> TODO()
            is SettingContract.Intent.ChangeLanguage -> TODO()
        }
    }

    private suspend fun handleError(e: Exception) {
        effects.emit(SettingContract.Effect.ShowError(e.message ?: "An error occurred"))
        _state.update { it.copy(
            isLoading = false,
            error = e.message
        ) }
    }
}