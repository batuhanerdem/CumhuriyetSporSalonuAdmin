package com.example.cumhuriyetsporsalonuadmin.utils

import android.util.Log
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

object LocalTimeConverter {
    private val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH.mm")
    fun String.toLocalTime(): LocalTime? {
        return try {
            LocalTime.parse(this, timeFormatter)
        } catch (e: DateTimeParseException) {
            Log.d(TAG, "Error parsing time: ${e.message}")
            null
        }
    }
}

const val TAG = "tag"