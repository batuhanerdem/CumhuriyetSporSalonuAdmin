package com.example.cumhuriyetsporsalonuadmin.utils

import android.content.Context
import androidx.annotation.StringRes

sealed interface Stringfy {
    data class Value(val value: String) : Stringfy
    data class Resource(@StringRes val resId: Int) : Stringfy

    fun getString(context: Context): String {
        return when (this) {
            is Value -> value
            is Resource -> context.getString(resId)
        }
    }

    companion object {
        fun String.stringfy() = Value(this)
        fun Int.stringfy() = Resource(this)
    }
}