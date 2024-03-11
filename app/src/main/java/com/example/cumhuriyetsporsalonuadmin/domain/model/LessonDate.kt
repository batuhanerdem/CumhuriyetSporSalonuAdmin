package com.example.cumhuriyetsporsalonuadmin.domain.model

import java.time.LocalTime

data class LessonDate(val day: Days, val startHour: LocalTime, val endHour: LocalTime) {
    fun convertToStringPair(): Pair<Pair<String, String>, Days> {
        return Pair(Pair(this.startHour.toString(), this.endHour.toString()), this.day)
    }

    companion object {
        fun isConflicting(firstLessonDate: LessonDate, secondLessonDate: LessonDate): Boolean {
            val firstStart = firstLessonDate.startHour
            val firstEnd = firstLessonDate.endHour
            val secondStart = secondLessonDate.startHour
            val secondEnd = secondLessonDate.endHour

            if (firstLessonDate.day != secondLessonDate.day) return false
            return !(firstEnd.isBefore(secondStart) || firstStart.isAfter(secondEnd))

        }
    }

}
