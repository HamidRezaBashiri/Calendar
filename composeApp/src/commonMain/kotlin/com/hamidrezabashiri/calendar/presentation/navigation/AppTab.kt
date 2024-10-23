package com.hamidrezabashiri.calendar.presentation.navigation

import cafe.adriel.voyager.core.screen.Screen
import com.hamidrezabashiri.calendar.presentation.screens.calendar.CalendarScreen
import com.hamidrezabashiri.calendar.presentation.screens.library.LibraryScreen
import com.hamidrezabashiri.calendar.presentation.screens.settings.SettingScreen

sealed class AppTab(val title: String, val content: () -> Screen) {
    object Library : AppTab("Library", { LibraryScreen() })
    object Settings : AppTab("Settings", { SettingScreen() })
    object Calendar : AppTab("Calendar", { CalendarScreen() })
}
