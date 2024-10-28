package com.hamidrezabashiri.calendar.presentation.navigation.tabs

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.hamidrezabashiri.calendar.presentation.navigation.BaseTab
import com.hamidrezabashiri.calendar.presentation.screens.calendar.CalendarScreen
import compose.icons.Octicons
import compose.icons.octicons.Calendar16


class CalendarTab : BaseTab() {
    override val options: TabOptions
        @Composable
        get() = TabOptions(
            index = 0u,
            title = "Calendar",
            icon = rememberVectorPainter(Octicons.Calendar16)
        )

    @Composable    override fun Content() {
        CalendarScreen()
    }
}

