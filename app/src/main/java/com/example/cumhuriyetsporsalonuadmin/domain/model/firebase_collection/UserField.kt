package com.example.cumhuriyetsporsalonuadmin.domain.model.firebase_collection

enum class UserField(val key: String) {
    UID("uid"),
    EMAIL("email"),
    NAME("name"),
    SURNAME("surname"),
    AGE("age"),
    HEIGHT("height"),
    WEIGHT("weight"),
    BMI("bmi"),
    IS_VERIFIED("verified"),
    LESSON_UIDS("lessonUids")
}