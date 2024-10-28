package com.hamidrezabashiri.calendar.presentation.screens.calendar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hamidrezabashiri.calendar.di.ViewModelProvider
import com.hamidrezabashiri.calendar.domain.model.CalendarEventModel
import com.hamidrezabashiri.calendar.presentation.screens.base.BaseScreen
import kotlinx.datetime.LocalDate

@Composable
fun CalendarScreen() {
    val viewModel = ViewModelProvider.provideCalendarViewModel()

    BaseScreen(
        viewModel = viewModel,
        effectHandler = { effect ->
            when (effect) {
                is CalendarContract.Effect.ShowError -> {
                    println("Error: ${effect.message}")
                    // Implement your error handling UI
                }
                is CalendarContract.Effect.NavigateToEventDetail -> {
                    println("Navigating to event ${effect.eventId}")
                    // Implement navigation
                }
                CalendarContract.Effect.ShowEventAddedSuccess -> {
                    println("Event added successfully")
                    // Show success message
                }
            }
        }
    ) { state, onIntent ->
        CalendarContent(
            state = state,
            onDateSelect = { date ->
//                onIntent(CalendarContract.Intent.SelectDate(date))
            },
            onEventClick = { eventId ->
//                onIntent(CalendarContract.Intent.NavigateToEvent(eventId))
            },
            onRefresh = {
                onIntent(CalendarContract.Intent.RefreshEvents)
            }
        )
    }
}

@Composable
private fun CalendarContent(
    state: CalendarContract.State,
    onDateSelect: (LocalDate) -> Unit,
    onEventClick: (String) -> Unit,
    onRefresh: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Calendar date picker
//            CalendarDatePicker(
//                selectedDate = state.selectedDate,
//                onDateSelect = onDateSelect
//            )

            // Events list
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
                state.error != null -> {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Error: ${state.error}")
                        Text(
                            "Tap to retry",
                            modifier = Modifier.clickable { onRefresh() }
                        )
                    }
                }
                state.events.isEmpty() -> {
                    Text(
                        "No events for ",
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
                else -> {
                    EventsList(
                        events = state.events,
                        onEventClick = onEventClick
                    )
                }
            }
        }
    }
}

@Composable
private fun EventsList(
    events: List<CalendarEventModel>,
    onEventClick: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        events.forEach { event ->
            EventItem(
                event = event,
                onClick = { onEventClick(event.id) }
            )
        }
    }
}

@Composable
private fun EventItem(
    event: CalendarEventModel,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(8.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = event.title)
            Text(text = "Start: ${event.startDate}")
            Text(text = "End: ${event.endDate}")
            event.description?.let { description ->
                Text(text = description)
            }
        }
    }
}