package com.hamidrezabashiri.calendar.presentation.screens.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.hamidrezabashiri.calendar.util.UiEffect
import com.hamidrezabashiri.calendar.util.UiIntent
import com.hamidrezabashiri.calendar.util.UiState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun <S : UiState, I : UiIntent, E : UiEffect> BaseScreen(
    viewModel: BaseViewModel<S, I, E>,
    effectHandler: (E) -> Unit,
    content: @Composable (state: S, onIntent: (I) -> Unit) -> Unit
) {
    val state by viewModel.state.collectAsState()

    // Handle effects
    LaunchedEffect(Unit) {
        viewModel.effects.onEach { effect ->
            effectHandler(effect)
        }.collect()
    }

    content(state) { intent ->
        viewModel.handleIntent(intent)
    }
}