package com.hamidrezabashiri.calendar.presentation.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions

abstract class BaseTab : Tab {
    abstract override val options: TabOptions
        @Composable get
}