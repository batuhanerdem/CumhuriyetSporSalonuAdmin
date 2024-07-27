package com.example.cumhuriyetsporsalonuadmin.domain.model.firebase_collection

enum class LessonField(val key: String) {
    UID("uid"),
    DAY("day"),
    NAME("name"),
    STUDENT_UIDS("studentUids"),
    REQUEST_UIDS("requestUids"),
    START_HOUR("startHour"),
    END_HOUR("endHour")
}