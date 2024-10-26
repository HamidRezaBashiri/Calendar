package com.hamidrezabashiri.calendar.presentation.screens.library


import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.hamidrezabashiri.calendar.di.ViewModelProvider
import com.hamidrezabashiri.calendar.presentation.screens.base.BaseScreen

@Composable
fun LibraryScreen() {
    val viewModel = ViewModelProvider.provideLibraryViewModel()

    BaseScreen(viewModel = viewModel, effectHandler = {
        when (it) {
            is LibraryContract.Effect.NavigateToBookReader -> TODO()
            is LibraryContract.Effect.ShowError -> TODO()
        }
    }) { state, onIntent ->
        // Your library UI here
        Text("Library Screen")
        state.books.forEach { book ->
//            BookItem(
//                event = event,
//                onClick = {
//                    onIntent(LibraryContract.Intent.LoadEvents(event.startDate))
//                }
//            )
        }
    }
}
