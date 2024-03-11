package com.example.cumhuriyetsporsalonuadmin.utils

object NullOrEmptyValidator {

    fun validate(vararg list: String): Boolean {
        for (item in list) {
            if (item.isEmpty()) return false
        }
        return true
    }

    fun validate(vararg list: Any?): Boolean {
        for (item in list) {
            if ((item is String && item.isEmpty()) || (item is List<Any?> && item.isEmpty()) || item == null) return false
        }
        return true
    }
}