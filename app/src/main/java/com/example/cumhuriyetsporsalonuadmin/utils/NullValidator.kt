package com.example.cumhuriyetsporsalonuadmin.utils

object NullValidator {
    fun validate(vararg list: Any?): Boolean {
        return list.all { it != null }
    }

    fun getNotNull(vararg list: Any?): List<Any> {
        return list.filterNotNull()
    }

}
