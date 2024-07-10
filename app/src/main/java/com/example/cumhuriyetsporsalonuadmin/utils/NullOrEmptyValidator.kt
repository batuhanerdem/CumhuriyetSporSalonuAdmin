package com.example.cumhuriyetsporsalonuadmin.utils

object NullOrEmptyValidator {

    fun validate(vararg list: String): Boolean {
        return list.all { it.isNotEmpty() }
    }

    fun validate(vararg list: Any?): Boolean {
        return list.all {
            when (it) {
                is String -> it.isNotEmpty()
                is List<*> -> it.isNotEmpty()
                else -> it != null
            }
        }
    }
}
