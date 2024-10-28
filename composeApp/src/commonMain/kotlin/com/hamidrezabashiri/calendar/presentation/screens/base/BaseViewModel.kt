package com.hamidrezabashiri.calendar.presentation.screens.base

import androidx.compose.runtime.Composable
import app.cash.molecule.RecompositionMode
import app.cash.molecule.launchMolecule
import com.hamidrezabashiri.calendar.util.UiEffect
import com.hamidrezabashiri.calendar.util.UiIntent
import com.hamidrezabashiri.calendar.util.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<S : UiState, I : UiIntent, E : UiEffect> {
    private val _viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    protected val viewModelScope: CoroutineScope get() = _viewModelScope

    private val _effects = MutableSharedFlow<E>(
        extraBufferCapacity = 1,
        replay = 0
    )
    val effects: SharedFlow<E> = _effects.asSharedFlow()

    private val _state = MutableStateFlow(getInitialState())
    val state: StateFlow<S> = _state

    init {
        initializeMolecule()
    }

    private fun initializeMolecule() {
        viewModelScope.launch {
            launchMolecule(
                mode = RecompositionMode.Immediate // Changed to Immediate mode to avoid frame clock issues
            ) {
                _state.update { produceUiState() }
            }
        }
    }

    // Abstract function to provide initial state
    abstract fun getInitialState(): S

    // Abstract function to handle state updates
    @Composable
    protected abstract fun produceUiState(): S

    // Protected method to emit effects
    protected fun emitEffect(effect: E) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }

    // Protected method to update state
    protected fun updateState(update: (S) -> S) {
        _state.update(update)
    }

    // Handle UI intents
    abstract fun handleIntent(intent: I)

    fun clear() {
        _viewModelScope.cancel()
    }
}