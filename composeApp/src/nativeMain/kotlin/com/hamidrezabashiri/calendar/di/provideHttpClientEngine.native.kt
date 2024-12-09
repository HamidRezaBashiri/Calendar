package com.hamidrezabashiri.calendar.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin

actual fun provideHttpClientEngine(): HttpClientEngine {
    return Darwin.create()

}