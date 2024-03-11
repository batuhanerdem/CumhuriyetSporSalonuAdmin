package com.example.cumhuriyetsporsalonuadmin.domain.model

import com.example.cumhuriyetsporsalonuadmin.R
import com.example.cumhuriyetsporsalonuadmin.domain.model.Days.Companion.toDay
import com.example.cumhuriyetsporsalonuadmin.utils.SelectableData
import com.example.cumhuriyetsporsalonuadmin.utils.Stringfy
import com.example.cumhuriyetsporsalonuadmin.utils.Stringfy.Companion.stringfy
import java.util.Locale

enum class Days(val number: Int, val stringIdAsStringfy: Stringfy) {
    MONDAY(1, R.string.monday.stringfy()),
    TUESDAY(2, R.string.tuesday.stringfy()),
    WEDNESDAY(3, R.string.wednesday.stringfy()),
    THURSDAY(4, R.string.thursday.stringfy()),
    FRIDAY(5, R.string.friday.stringfy()),
    SATURDAY(6, R.string.saturday.stringfy()),
    SUNDAY(7, R.string.sunday.stringfy());

    fun toSelectable(): SelectableData<Days> {
        return SelectableData(this, false)
    }

    companion object {
        fun Int.toDay(): Days? {
            return when (this) {
                1 -> MONDAY
                2 -> TUESDAY
                3 -> WEDNESDAY
                4 -> THURSDAY
                5 -> FRIDAY
                6 -> SATURDAY
                7 -> SUNDAY
                else -> null
            }
        }
    }
}
