package com.hamidrezabashiri.calendar.di

import com.hamidrezabashiri.calendar.data.repository.CalendarEventRepositoryImpl
import com.hamidrezabashiri.calendar.data.source.local.CalendarEventLocalDataSource
import com.hamidrezabashiri.calendar.data.source.local.CalendarEventLocalDataSourceImpl
import com.hamidrezabashiri.calendar.data.source.local.RealmDatabase
import com.hamidrezabashiri.calendar.data.source.remote.CalendarEventRemoteDataSource
import com.hamidrezabashiri.calendar.data.source.remote.CalendarEventRemoteDataSourceImpl
import com.hamidrezabashiri.calendar.domain.repository.CalendarEventRepository
import com.hamidrezabashiri.calendar.domain.usecase.CalendarUseCases
import com.hamidrezabashiri.calendar.domain.usecase.event.AddEventUseCase
import com.hamidrezabashiri.calendar.domain.usecase.event.DeleteEventUseCase
import com.hamidrezabashiri.calendar.domain.usecase.event.GetEventDetailsUseCase
import com.hamidrezabashiri.calendar.domain.usecase.event.GetEventsForDateUseCase
import com.hamidrezabashiri.calendar.domain.usecase.event.GetEventsForMonthUseCase
import com.hamidrezabashiri.calendar.domain.usecase.event.UpdateEventUseCase
import com.hamidrezabashiri.calendar.presentation.screens.calendar.CalendarViewModel
import com.hamidrezabashiri.calendar.presentation.screens.library.LibraryViewModel
import com.hamidrezabashiri.calendar.presentation.screens.settings.SettingViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module


val dataModule = module {
    single { RealmDatabase.getInstance() }
    single<CalendarEventLocalDataSource> { CalendarEventLocalDataSourceImpl(get()) }
    single<CalendarEventRemoteDataSource> { CalendarEventRemoteDataSourceImpl() }
    single<CalendarEventRepository> { CalendarEventRepositoryImpl(get(), get()) }
}
val useCaseModule = module {
    factory { GetEventsForDateUseCase(get()) }
    factory { GetEventsForMonthUseCase(get()) }
    factory { AddEventUseCase(get()) }
    factory { UpdateEventUseCase(get()) }
    factory { DeleteEventUseCase(get()) }
    factory { GetEventDetailsUseCase(get()) }

    factory {
        CalendarUseCases(
            getEventsForDate = get(),
            getEventsForMonth = get(),
            addEvent = get(),
            updateEvent = get(),
            deleteEvent = get(),
            getEventDetails = get()
        )
    }
}

val viewModelModule = module {
    factory { CalendarViewModel(get()) }
    factory { LibraryViewModel() }
    factory { SettingViewModel() }
    // Add other ViewModels here
}

fun initKoin() {
    startKoin {
        modules( dataModule, useCaseModule,viewModelModule)
    }
}

// Inline functions for better type safety
inline fun <reified T : Any> Module.singleOf(crossinline constructor: () -> T) {
    single { constructor() }
}

inline fun <reified T : Any> Module.factoryOf(crossinline constructor: () -> T) {
    factory { constructor() }
}