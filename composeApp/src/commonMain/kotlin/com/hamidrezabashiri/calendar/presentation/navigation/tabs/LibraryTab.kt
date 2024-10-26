package com.hamidrezabashiri.calendar.presentation.navigation.tabs

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.hamidrezabashiri.calendar.presentation.navigation.BaseTab
import com.hamidrezabashiri.calendar.presentation.screens.library.LibraryScreen

class LibraryTab : BaseTab() {
    override val options: TabOptions
        @Composable
        get() = TabOptions(
            index = 1u,
            title = "Library",
//            icon =
        )

    @Composable
    override fun Content() {
        LibraryScreen()
    }
}