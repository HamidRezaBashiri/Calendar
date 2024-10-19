package com.hamidrezabashiri.calendar.data.source.local


import com.hamidrezabashiri.calendar.data.entity.CalendarEventEntity
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

object RealmDatabase {
    private val realm: Realm by lazy {
        val config = RealmConfiguration.Builder(schema = setOf(CalendarEventEntity::class))
            .name("calendar_app.realm")
            .schemaVersion(1)
            .build()
        Realm.open(config)
    }

    fun getInstance(): Realm = realm
}