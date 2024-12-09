package com.hamidrezabashiri.calendar.data.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HolidayEntity(
    val country: String,
    val iso: String,
    val year: Int,
    val date: String,
    val day: String,
    val name: String,
    @SerialName("type")
    val eventType: String,
)