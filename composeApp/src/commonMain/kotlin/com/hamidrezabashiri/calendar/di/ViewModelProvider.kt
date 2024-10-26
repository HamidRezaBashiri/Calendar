package com.hamidrezabashiri.calendar.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.hamidrezabashiri.calendar.presentation.screens.calendar.CalendarViewModel
import com.hamidrezabashiri.calendar.presentation.screens.library.LibraryViewModel
import com.hamidrezabashiri.calendar.presentation.screens.settings.SettingViewModel
import org.koin.mp.KoinPlatform

// In your di package
object ViewModelProvider {
    private val koin = KoinPlatform.getKoin()

    @Composable
    fun provideCalendarViewModel(): CalendarViewModel {
        return remember { koin.get<CalendarViewModel>() }
    }
    @Composable
    fun provideLibraryViewModel(): LibraryViewModel {
        return remember { koin.get<LibraryViewModel>() }
    }
    @Composable
    fun provideSettingViewModel(): SettingViewModel {
        return remember { koin.get<SettingViewModel>() }
    }
}