package com.hamidrezabashiri.calendar.presentation.navigation.tabs

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.hamidrezabashiri.calendar.presentation.navigation.BaseTab
import com.hamidrezabashiri.calendar.presentation.screens.calendar.CalendarScreen


class CalendarTab : BaseTab() {
    override val options: TabOptions
        @Composable
        get() = TabOptions(
            index = 0u,
            title = "Calendar",
//            icon = Icons.Default.Settings
        )

    @Composable
    override fun Content() {
        CalendarScreen()
    }
}

