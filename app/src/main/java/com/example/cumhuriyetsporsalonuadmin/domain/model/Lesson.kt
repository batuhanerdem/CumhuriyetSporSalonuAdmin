package com.example.cumhuriyetsporsalonuadmin.domain.model

data class Lesson(
    val uid: String,
    var name: String,
    var day: String,
    var timeBegin: String, // custom class called LessonHours, string to date
    var timeEnd: String,
    var studentUids: List<String> = emptyList()
)

