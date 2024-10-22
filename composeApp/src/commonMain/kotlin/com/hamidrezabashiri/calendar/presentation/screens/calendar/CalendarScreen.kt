package com.hamidrezabashiri.calendar.presentation.screens.calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import org.koin.compose.viewmodel.koinViewModel

class CalendarScreen : Screen {

    @Composable
    override fun Content() {
        CalendarScreen()
    }

    @Composable
    fun CalendarScreen(
        viewModel: CalendarViewModel = koinViewModel()
    ) {
        val state by viewModel.state.collectAsState()

        Column {
            // Calendar view
//            CalendarView(
//                selectedDate = state.selectedDate,
//                events = state.events,
//                onDateSelect = viewModel::onDateSelected
//            )

            // Events list
            LazyColumn {
//                items(state.events) { event ->
//                    EventItem(
//                        event = event,
//                        onDelete = { viewModel.deleteEvent(event.id) }
//                    )
//                }
            }

            // Add event bottom sheet
//            if (state.isAddingEvent) {
//                AddEventBottomSheet(
//                    onAddEvent = viewModel::addEvent,
//                    onDismiss = viewModel::hideAddEventSheet
//                )
//            }
        }
    }
}