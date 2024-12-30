package com.hamidrezabashiri.calendar.presentation.screens.calendar

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hamidrezabashiri.calendar.di.ViewModelProvider
import com.hamidrezabashiri.calendar.domain.model.CalendarEventModel
import com.hamidrezabashiri.calendar.presentation.screens.addEvent.EventDetailsForm
import com.hamidrezabashiri.calendar.presentation.screens.base.BaseScreen
import com.hamidrezabashiri.calendar.util.CalendarConstants.INITIAL_PAGE_INDEX
import com.hamidrezabashiri.calendar.util.CalendarConstants.MAX_PAGES
import io.wojciechosak.calendar.config.DayState
import io.wojciechosak.calendar.config.rememberCalendarState
import io.wojciechosak.calendar.view.CalendarView
import io.wojciechosak.calendar.view.HorizontalCalendarView
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

@Composable
fun CalendarScreen() {
    val viewModel = ViewModelProvider.provideCalendarViewModel()
    val coroutineScope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    BaseScreen(viewModel = viewModel, effectHandler = { effect ->
        when (effect) {
            is CalendarContract.Effect.ShowError -> {
                println("Error: ${effect.message}")
            }

            is CalendarContract.Effect.NavigateToEventDetail -> {
                println("Navigating to event ${effect.eventId}")
            }

            CalendarContract.Effect.ShowEventAddedSuccess -> {
                println("Event added successfully")
                viewModel.handleIntent(CalendarContract.Intent.LoadEvents(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date))
            }
        }
    }) { state, onIntent ->
        Box(Modifier.fillMaxSize()) {
            CalendarContent(
                state = state,
                onDateSelect = { date -> },
                onEventClick = { eventId -> },
                onRefresh = { onIntent(CalendarContract.Intent.RefreshEvents)},
                onIntent = onIntent
            )

            // Floating Action Button for adding an event
//            FloatingActionButton(
//                onClick = {
//                    showBottomSheet = true
//
//                },
//                modifier = Modifier
//                    .align(Alignment.BottomEnd)
//                    .padding(16.dp),
//                content = {
//                    Icon(
//                        imageVector = Icons.Filled.Add,
//                        contentDescription = "Add Event"
//                    )
//                }
//            )

            if (showBottomSheet) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f))
                        .clickable(onClick = {
                            showBottomSheet = false
                        }) // Dismiss on outside click
                ) {
                    EventDetailsForm(
                        event = state.event, // Access the event object from the state
                        onEventChange = { updatedEvent ->
                            onIntent(CalendarContract.Intent.UpdateEventForEditing(updatedEvent))
                        },
                        onSave = {
                            coroutineScope.launch {
                                // Handle save: hide the bottom sheet and trigger AddEvent intent
                                showBottomSheet = false
                                viewModel.handleIntent(CalendarContract.Intent.AddEvent(state.event))
                            }
                        },
                        onCancel = {
                            showBottomSheet = false // Close the bottom sheet without saving
                        }
                    )
                }
            }

        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CalendarContent(
    state: CalendarContract.State,
    onDateSelect: (LocalDate) -> Unit,
    onEventClick: (String) -> Unit,
    onRefresh: () -> Unit,
    onIntent: (CalendarContract.Intent) -> Unit
) {
    val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val pagerState = rememberPagerState(initialPage = INITIAL_PAGE_INDEX, pageCount = { MAX_PAGES })
    val scope = rememberCoroutineScope()
    LaunchedEffect(pagerState.currentPage) {
        scope.launch {  // Use a coroutine for heavy work
            val monthOffset = pagerState.currentPage - INITIAL_PAGE_INDEX
            val newDate = currentDate.plus(DatePeriod(months = monthOffset))
            onIntent(CalendarContract.Intent.UpdateMonthOffset(monthOffset))
//            onIntent(CalendarContract.Intent.LoadEvents(newDate))

            if (newDate.year != currentDate.year) {
                onIntent(CalendarContract.Intent.FetchHolidays(newDate.year))
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().weight(1f).wrapContentHeight()
                .background(Color.White.copy(0.05f))
        ) {
            HorizontalCalendarView(startDate = currentDate,
                pagerState = pagerState,
                modifier = Modifier.align(Alignment.TopCenter).background(Color.White.copy(0.05f))
                    .fillMaxSize().animateContentSize().wrapContentHeight(),
                calendarView = { monthOffset ->

                    CalendarView(modifier = Modifier.fillMaxSize(), day = { dayState ->
                        CustomCalendarDay(
                            state = dayState,
                            selectedDate = state.selectedDate,
                            events = state.events,
                            onDateSelected = { date ->
                                onIntent(CalendarContract.Intent.SelectDate(date))
                            }
                        )
                    }, config = rememberCalendarState(
                        startDate = currentDate, monthOffset = monthOffset
                    ), header = { month, year ->

                        val scope = rememberCoroutineScope()

                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .padding( 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.size(36.dp).border(
                                    2.dp,
                                    shape = RoundedCornerShape(10.dp),
                                    color = colors.onSurface.copy(0.1f)
                                ).clickable {
                                    scope.launch {
                                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                    }
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                    contentDescription = "Previous month",
                                    tint = colors.onSurface,
                                    modifier = Modifier.size(24.dp) // Larger icon size
                                )
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "$month",
                                    style = MaterialTheme.typography.h6,
                                    color = colors.onSurface
                                )
                                Text(
                                    text = "$year",
                                    style = MaterialTheme.typography.subtitle1,
                                    color = colors.onSurface.copy(0.5f)
                                )

                            }
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.size(36.dp).border(
                                    2.dp,
                                    shape = RoundedCornerShape(10.dp),
                                    color = colors.onSurface.copy(0.1f)
                                ).clickable {
                                    scope.launch {
                                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                    }
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = "Next month",
                                    tint = colors.onSurface,
                                    modifier = Modifier.size(24.dp) // Larger icon size
                                )
                            }

                        }
                    })
                })
        }

        Box(
            modifier = Modifier.fillMaxWidth().weight(1f)
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
//                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                state.error != null -> {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Error: ${state.error}")
                        Text("Tap to retry", modifier = Modifier.clickable { onRefresh() })
                    }
                }

                state.events.isEmpty() -> {
                    Text(
                        "No events for selected date",
//                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                else -> {
                    EventsList(
                        events = state.events, onEventClick = onEventClick
                    )
                }
            }
        }
    }
}


@Composable
fun CustomCalendarDay(
    state: DayState,
    selectedDate: LocalDate?,
    events: List<CalendarEventModel>,
    onDateSelected: (LocalDate) -> Unit
) {
    var isInteractionEnabled by remember { mutableStateOf(true) }
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val hasEvents = events.any { it.startDate == state.date }

    Box(
        modifier = Modifier
            .size(46.dp)
            .clip(CircleShape)
            .clickable(
                enabled = isInteractionEnabled,
                onClick = {
                    isInteractionEnabled = false
                    onDateSelected(state.date)
                }
            )
            .background(
                when {
                    selectedDate == state.date -> colors.primary
                    state.date == today -> colors.primary.copy(alpha = 0.2f)
                    else -> Color.Transparent
                }
            )
            .padding(2.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = state.date.dayOfMonth.toString(),
                color = when {
                    selectedDate == state.date -> Color.White
                    state.date == today -> colors.primary
                    state.isForNextMonth || state.isForPreviousMonth -> 
                        colors.onSurface.copy(alpha = 0.5f)
                    else -> colors.onSurface
                },
                style = MaterialTheme.typography.body1.copy(
                    fontWeight = when {
                        selectedDate == state.date -> FontWeight.Medium
                        state.date == today -> FontWeight.Medium
                        else -> FontWeight.Normal
                    }
                )
            )
            
            if (hasEvents) {
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .clip(CircleShape)
                        .background(Color.Green.copy(alpha = 0.8f))
                )
            }
        }
    }
}


@Composable
private fun EventsList(
    events: List<CalendarEventModel>, onEventClick: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        events.forEach { event ->
            EventItem(event = event, onClick = { onEventClick(event.id) })
        }
    }
}

@Composable
private fun EventItem(
    event: CalendarEventModel, onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(8.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = event.title)
            Text(text = "Date: ${event.startDate}")
            Text(text = event.category)
            Text(text = event.description)
        }
    }
}
