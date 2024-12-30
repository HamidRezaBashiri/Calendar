package com.hamidrezabashiri.calendar.presentation.screens.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import com.hamidrezabashiri.calendar.util.UiEffect
import com.hamidrezabashiri.calendar.util.UiIntent
import com.hamidrezabashiri.calendar.util.UiState
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun <S : UiState, I : UiIntent, E : UiEffect> BaseScreen(
    viewModel: BaseViewModel<S, I, E>,
    effectHandler: (E) -> Unit,
    content: @Composable (state: S, onIntent: (I) -> Unit) -> Unit
) {
    // Collect state with error handling and logging
    val state by viewModel.state.collectAsState()

    // Handle effects with error handling
    LaunchedEffect(viewModel) {
        viewModel.effects
            .catch { throwable ->
                println("Error in effects flow: ${throwable.message}")
            }
            .onEach { effect ->
                try {
                    effectHandler(effect)
                } catch (e: Exception) {
                    println("Error handling effect: ${e.message}")
                }
            }
            .collect()
    }

    // Wrap content in try-catch for debugging
        content(state) { intent ->
            try {
                viewModel.handleIntent(intent)
            } catch (e: Exception) {
                println("Error handling intent: ${e.message}")
            }
        }

}