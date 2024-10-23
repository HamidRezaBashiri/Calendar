package com.hamidrezabashiri.calendar.presentation.screens.base

import androidx.compose.runtime.Composable
import app.cash.molecule.RecompositionMode
import app.cash.molecule.moleculeFlow
import com.hamidrezabashiri.calendar.util.UiEffect
import com.hamidrezabashiri.calendar.util.UiIntent
import com.hamidrezabashiri.calendar.util.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

abstract class BaseViewModel<S : UiState, I : UiIntent, E : UiEffect> {
    protected val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main) // Changed to protected

    // The current state of the UI
    abstract val state: StateFlow<S>

    // Side effects (one-time events like navigation, showing toast)
    abstract val effects: MutableSharedFlow<E> // Changed to MutableSharedFlow
    val uiEffects: Flow<E> get() = effects

    // Handle UI intents
    abstract fun handleIntent(intent: I)

    // Molecule state production with StateFlow conversion
    protected fun produceState(
        initialState: S,
        block: @Composable () -> S
    ): StateFlow<S> {
        return moleculeFlow(RecompositionMode.ContextClock, block)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = initialState
            )
    }

    fun clear() {
        viewModelScope.cancel()
    }
}