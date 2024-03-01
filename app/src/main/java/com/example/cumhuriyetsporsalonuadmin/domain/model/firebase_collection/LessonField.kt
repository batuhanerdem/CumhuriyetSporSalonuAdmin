package com.example.cumhuriyetsporsalonuadmin.domain.model.firebase_collection

enum class LessonField(val key: String) {
    UID("uid"),
    DAY("day"),
    NAME("name"),
    STUDENT_UIDS("studentUids"),
    TIME_BEGIN("timeBegin"),
    TIME_END("timeEnd")
}