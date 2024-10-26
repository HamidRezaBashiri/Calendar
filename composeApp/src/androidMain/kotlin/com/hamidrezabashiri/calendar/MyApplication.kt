package com.hamidrezabashiri.calendar

import android.app.Application
import com.hamidrezabashiri.calendar.di.initKoin
import org.koin.core.component.KoinComponent

class MyApplication: Application(), KoinComponent {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }
}