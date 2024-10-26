package com.hamidrezabashiri.calendar.presentation.screens.settings

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.hamidrezabashiri.calendar.di.ViewModelProvider
import com.hamidrezabashiri.calendar.presentation.screens.base.BaseScreen

@Composable
fun SettingScreen(){
    val viewModel = ViewModelProvider.provideSettingViewModel()

    BaseScreen(viewModel=viewModel, effectHandler = {
        when (it) {
            is SettingContract.Effect.ShowToast -> TODO()
            is SettingContract.Effect.ShowError -> TODO()
        }
    }) { state, onIntent ->

        if (state.isLoading) {
            CircularProgressIndicator()
        }
        Text("Setting Screen")
//        state.books.forEach { book ->
//            BookItem(
//                event = event,
//                onClick = {
//                    onIntent(LibraryContract.Intent.LoadEvents(event.startDate))
//                }
//            )
//        }
    }

}