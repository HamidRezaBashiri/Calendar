package com.hamidrezabashiri.calendar.presentation.navigation.tabs

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.hamidrezabashiri.calendar.presentation.navigation.BaseTab
import com.hamidrezabashiri.calendar.presentation.screens.library.LibraryScreen
import compose.icons.Octicons
import compose.icons.octicons.Book16

class LibraryTab : BaseTab() {
    override val options: TabOptions
        @Composable
        get() = TabOptions(
            index = 1u,
            title = "Library",
            icon = rememberVectorPainter(Octicons.Book16)

        )

    @Composable
    override fun Content() {
        println("LibraryTab Content called") // Add logging

        LibraryScreen()
    }
}