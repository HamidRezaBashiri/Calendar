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
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn

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
            CalendarContent(state = state, onDateSelect = { date ->
                // onIntent(CalendarContract.Intent.SelectDate(date))
            }, onEventClick = { eventId ->
                // onIntent(CalendarContract.Intent.NavigateToEvent(eventId))
            }, onRefresh = {
                onIntent(CalendarContract.Intent.RefreshEvents)
            })

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
    onRefresh: () -> Unit
) {
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val pagerState = rememberPagerState(initialPage = INITIAL_PAGE_INDEX, pageCount = { MAX_PAGES })

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
                        CustomCalendarDay(state = dayState,
                            selectedDate = selectedDate,
                            onDateSelected = { date ->
                                selectedDate = if (selectedDate == date) {
                                    null
                                } else date
                                onDateSelect(date)
                            })
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
    state: DayState, selectedDate: LocalDate?, onDateSelected: (LocalDate) -> Unit
) {
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    val isToday = state.date == today
    val isSelected = selectedDate == state.date
    val isOutsideMonth = state.isForNextMonth || state.isForPreviousMonth


    val backgroundColor = when {
        isSelected -> colors.primary
        isToday -> if (colors.isLight) {
            colors.primary.copy(alpha = 0.1f)
        } else {
            colors.primary.copy(alpha = 0.2f)
        }

        else -> Color.Transparent
    }

    val textColor = when {
        isSelected -> Color.White
        isToday -> colors.primary
        isOutsideMonth -> if (colors.isLight) {
            Color(0xFF9AA0A6).copy(alpha = 0.5f)
        } else {
            Color.White.copy(alpha = 0.5f)
        }

        else -> if (colors.isLight) {
            Color(0xFF3C4043)
        } else {
            Color.White.copy(alpha = 0.87f)
        }
    }

    Box(
        modifier = Modifier.padding(2.dp).size(44.dp).clip(CircleShape).background(backgroundColor)
            .then(
                if (isToday && !isSelected) {
                    Modifier.background(
                        color = if (colors.isLight) {
                            colors.primary.copy(alpha = 0.1f)
                        } else {
                            colors.primary.copy(alpha = 0.2f)
                        }, shape = CircleShape
                    )
                } else Modifier
            ).clickable(enabled = state.enabled, onClick = { onDateSelected(state.date) }),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = state.date.dayOfMonth.toString(),
            color = textColor,
            style = MaterialTheme.typography.body1.copy(
                fontWeight = when {
                    isSelected || isToday -> FontWeight.Medium
                    else -> FontWeight.Normal
                }
            )
        )
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
