package com.hamidrezabashiri.calendar.di

import com.hamidrezabashiri.calendar.data.repository.EventRepository
import com.hamidrezabashiri.calendar.domain.repository.IEventRepository
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

val appModule = module {
//    singleOf(::LocalDataSource)
//    singleOf(::RemoteDataSource)
    singleOf<IEventRepository>(::EventRepository)
//    factoryOf(::GetUserUseCase)
//    factoryOf(::HomeViewModel)
//    factoryOf(::DetailViewModel)
}

fun initKoin() {
    startKoin {
        modules(appModule)
    }
}

// Inline functions for better type safety
inline fun <reified T : Any> Module.singleOf(crossinline constructor: () -> T) {
    single { constructor() }
}

inline fun <reified T : Any> Module.factoryOf(crossinline constructor: () -> T) {
    factory { constructor() }
}