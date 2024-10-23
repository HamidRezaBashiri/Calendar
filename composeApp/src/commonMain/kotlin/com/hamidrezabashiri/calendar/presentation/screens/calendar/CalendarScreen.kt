package com.hamidrezabashiri.calendar.presentation.screens.calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.hamidrezabashiri.calendar.presentation.screens.base.BaseScreen


@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel,
//    onNavigate: (String) -> Unit
) {
    BaseScreen(
        viewModel = viewModel,
        effectHandler = { effect ->
            when (effect) {
                is CalendarContract.Effect.ShowError -> {
                    // Show error toast
                }
                is CalendarContract.Effect.NavigateToEventDetail -> {
//                    onNavigate(effect.eventId)
                }
                CalendarContract.Effect.ShowEventAddedSuccess -> {
                    // Show success message
                }
            }
        }
    ) { state, onIntent ->
        // Your calendar UI here
        Column {
            if (state.isLoading) {
                CircularProgressIndicator()
            }

            // Calendar content
            state.events.forEach { event ->
                Text(event.title)
//                EventItem(
//                    event = event,
//                    onClick = {
//                        onIntent(CalendarContract.Intent.LoadEvents(event.startDate))
//                    }
//                )
            }
        }
    }
}