package com.example.cumhuriyetsporsalonuadmin.utils

import java.util.Locale


object StringUtils {
    fun capitalizeFirstLetters(name: String?): String? {
        return name?.split(" ")?.joinToString(" ") {
            it.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            }
        }
    }
}