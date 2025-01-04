package com.hamidrezabashiri.calendar.presentation.common.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hamidrezabashiri.calendar.util.CalendarConstants.INITIAL_PAGE_INDEX
import com.hamidrezabashiri.calendar.util.CalendarConstants.MAX_PAGES
import io.wojciechosak.calendar.animation.CalendarAnimator
import io.wojciechosak.calendar.config.rememberCalendarState
import io.wojciechosak.calendar.view.CalendarDay
import io.wojciechosak.calendar.view.CalendarView
import kotlinx.datetime.LocalDate

/**
 * Composable function to display a horizontal calendar view.
 *
 * @param startDate The start date of the calendar.
 * @param pagerState The PagerState used to control the horizontal paging behavior of the calendar.
 * @param modifier The modifier for styling and layout of the calendar.
 * @param pageSize The size of each page in the calendar. Default is [PageSize.Fill].
 * @param beyondBoundsPageCount The number of pages to keep loaded beyond the visible bounds. Default is 0.
 * @param contentPadding The padding applied around the content of the day cell.
 * @param calendarAnimator The animator used for animating calendar transitions.
 * @param calendarView The composable function to display the content of each calendar page.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MyHorizontalCalendarView(
    startDate: LocalDate,
    pagerState: PagerState = rememberPagerState(
        initialPage = INITIAL_PAGE_INDEX,
        pageCount = { MAX_PAGES },
    ),
    modifier: Modifier = Modifier,
    pageSize: PageSize = PageSize.Fill,
    beyondBoundsPageCount: Int = 0,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    calendarAnimator: CalendarAnimator = CalendarAnimator(startDate),
    calendarView: @Composable (monthOffset: Int) -> Unit = {
        CalendarView(
            day = { dayState ->
                CalendarDay(
                    state = dayState,
                    onClick = { },
                )
            },
            config = rememberCalendarState(
                startDate = startDate,
                monthOffset = it,
            ),

        )
    },
) {
    HorizontalPager(
        state = pagerState,
        modifier = modifier,
        pageSize = pageSize,
        beyondBoundsPageCount = beyondBoundsPageCount,
        contentPadding = contentPadding,
        verticalAlignment = Alignment.Top
    ) {
        val index = it - INITIAL_PAGE_INDEX

        calendarView(index)
    }
}
