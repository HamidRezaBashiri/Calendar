package com.hamidrezabashiri.calendar.presentation.screens.calendar

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hamidrezabashiri.calendar.di.ViewModelProvider
import com.hamidrezabashiri.calendar.domain.model.CalendarEventModel
import com.hamidrezabashiri.calendar.presentation.common.components.EventIndicator
import com.hamidrezabashiri.calendar.presentation.common.components.EventMapper
import com.hamidrezabashiri.calendar.presentation.common.components.MyHorizontalCalendarView
import com.hamidrezabashiri.calendar.presentation.screens.addEvent.EventDetailsForm
import com.hamidrezabashiri.calendar.presentation.screens.base.BaseScreen
import com.hamidrezabashiri.calendar.util.CalendarConstants.INITIAL_PAGE_INDEX
import com.hamidrezabashiri.calendar.util.CalendarConstants.MAX_PAGES
import io.ktor.utils.io.charsets.Charsets
import io.ktor.utils.io.charsets.name
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
        Box(Modifier.fillMaxSize() ,contentAlignment = Alignment.TopStart // Ensure content aligns to the top
            ,) {
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
                Box(modifier = Modifier
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

            if (newDate.year != currentDate.year) {
                onIntent(CalendarContract.Intent.FetchHolidays(newDate.year))
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Color.Transparent),
        verticalArrangement = Arrangement.Top  // This ensures content starts from top
    ) {
        Card(
            modifier = Modifier.padding(4.dp)
                .background(Color.Transparent)
                .fillMaxWidth()
              ,
            elevation = 4.dp,
            shape = RoundedCornerShape(16.dp),
            backgroundColor = colors.surface,
            border = BorderStroke(1.dp, colors.onSurface.copy(alpha = 0.12f))
        ){

            MyHorizontalCalendarView(
                startDate = currentDate,
                beyondBoundsPageCount = 0,
                pagerState = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(align = Alignment.Top)
                    .animateContentSize()
                    .padding(bottom = 8.dp)
                ,

                calendarView = { monthOffset ->

                    CalendarView(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalArrangement = Arrangement.Top,
                        day = { dayState ->
                            val indicators = EventMapper.mapEventsToIndicators(state.events, dayState.date)

                            CustomCalendarDay(
                                state = dayState,
                                isSelected = state.selectedDate == dayState.date,
                                eventIndicators = indicators,
                                onDateSelected = { date ->
                                    onIntent(CalendarContract.Intent.SelectDate(date))
                                }
                            )
                        }, config = rememberCalendarState(
                            startDate = currentDate, monthOffset = monthOffset
                        ),
                        header = { month, year ->
                            Row(
                                modifier = Modifier.fillMaxWidth()
                                    .padding(top = 16.dp, bottom = 26.dp )
                                    .padding(horizontal = 16.dp),

                                horizontalArrangement = Arrangement.SpaceBetween,
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
                        },
                        dayOfWeekLabel = { dayOfWeek ->
                            Text(dayOfWeek.name.substring(IntRange(0, 2)),
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth(),
                                color = colors.onSurface
                            )
                        })
                })
        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(1f)
        ) {
            when {
                state.isLoading -> {
                    item {
                        CircularProgressIndicator()
                    }
                }
                state.error != null -> {
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Error: ${state.error}")
                            Text("Tap to retry", modifier = Modifier.clickable { onRefresh() })
                        }
                    }
                }
                state.events.isEmpty() -> {
                    item {
                        Text("No events for selected date")
                    }
                }
                else -> {
                    items(state.events) { event ->
                        EventItem(event = event, onClick = { onEventClick(event.id) })
                    }
                    item {
                        Spacer(modifier = Modifier.height(120.dp))
                    }
                }
            }
        }
    }
}


@Composable
fun CustomCalendarDay(
    state: DayState,
    isSelected: Boolean,
    eventIndicators: List<EventIndicator>,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier

) {

    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    Box(
        modifier = modifier.fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,  // This removes the ripple effect
                onClick = { onDateSelected(state.date) }
            )
            .padding(2.dp),
        contentAlignment = Alignment.Center
    ) {        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

        DayNumber(
            date = state.date,
            isSelected = isSelected,
            isToday = state.date == today,
            isOutOfBounds = state.isForNextMonth || state.isForPreviousMonth
        )

        EventIndicators(indicators = eventIndicators)

        }
    }
}

@Composable
fun DayNumber(
    date: LocalDate,
    isSelected: Boolean,
    isToday: Boolean,
    isOutOfBounds: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(32.dp)
            .clip(RoundedCornerShape(32))
            .background(
                when {
                    isSelected -> colors.primary
                    isToday -> colors.primary.copy(alpha = 0.2f)
                    else -> Color.Transparent
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = date.dayOfMonth.toString(),
            color = when {
                isSelected -> Color.White
                isToday -> colors.primary
                isOutOfBounds -> colors.onSurface.copy(alpha = 0.5f)
                else -> colors.onSurface
            },
            style = MaterialTheme.typography.body1.copy(
                fontWeight = if (isSelected || isToday) FontWeight.Medium else FontWeight.Normal
            ),
            textAlign = TextAlign.Center
        )
    }
}
@Composable
fun EventIndicators(
    indicators: List<EventIndicator>,
    modifier: Modifier = Modifier
) {
    if (indicators.isEmpty()) {
        Spacer(modifier = Modifier.size(5.dp))
        return
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        modifier = modifier.padding(top = 2.dp)
    ) {
        indicators.forEach { indicator ->
            Box(
                modifier = Modifier
                    .size(5.dp)
                    .border(1.dp, indicator.color, CircleShape)
            )
        }
    }
}


@Composable
private fun EventItem(
    event: CalendarEventModel, onClick: () -> Unit
) {
    val formattedDate = "${event.startDate.dayOfMonth} ${event.startDate.month.name.take(3)}"
    val uriHandler = LocalUriHandler.current
    val searchQuery = "${event.title} ${event.startDate.year} Tajikistan"
//    val encodedQuery = URLEncoder.encode(searchQuery, Charsets.UTF_8.toString())
    val searchUrl = "https://www.google.com/search?q=$searchQuery"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick)
            ,
        elevation = 0.dp,
        shape = RoundedCornerShape(16.dp),
        backgroundColor = colors.surface,
        border = BorderStroke(1.dp, colors.onSurface.copy(alpha = 0.12f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Event indicator dot
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(10.dp)
                        .border(2.dp,  when(event.category) {
                            "PUBLIC_HOLIDAY" -> EventIndicator.PUBLIC_HOLIDAY.color
                            "OBSERVANCE" -> EventIndicator.OBSERVANCE.color
                            "SEASON" -> EventIndicator.SEASON.color
                            else -> colors.onSurface.copy(alpha = 0.3f)
                        }, CircleShape)

                )

                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.body2,
                    color = colors.onSurface.copy(alpha = 0.6f)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = event.title,
                style = MaterialTheme.typography.h6,
                color = colors.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = event.category,
                style = MaterialTheme.typography.caption,
                color = colors.onSurface.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(8.dp))
//            Text(
//                text = event.description,
//                style = MaterialTheme.typography.body1,
//                color = colors.onSurface.copy(alpha = 0.8f)
//            )
//            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Search More ➞",
                style = MaterialTheme.typography.button,
                color = colors.primary,
                modifier = Modifier
                    .clickable { uriHandler.openUri(searchUrl) }
                    .fillMaxWidth(),
                textAlign = TextAlign.End
            )
        }
    }
}
