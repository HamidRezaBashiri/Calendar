package com.hamidrezabashiri.calendar.di

import com.hamidrezabashiri.calendar.data.model.CalendarEvent
import com.hamidrezabashiri.calendar.data.repository.CalendarEventRepository
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

val databaseModule = DI.Module("database") {
    bind<Realm>() with singleton {
        val config = RealmConfiguration.Builder(schema = setOf(CalendarEvent::class))
            .name("calendar.realm")
            .build()
        Realm.open(config)
    }

    bind<CalendarEventRepository>() with singleton { CalendarEventRepository(instance()) }
}

val appModule = DI.Module("app") {
    import(databaseModule)
    // Add other modules here as your app grows
}