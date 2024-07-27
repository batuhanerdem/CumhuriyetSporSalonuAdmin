package com.example.cumhuriyetsporsalonuadmin.domain.model

data class Lesson(
    val uid: String,
    var name: String,
    val lessonDate: LessonDate,
    var studentUids: List<String> = emptyList(),
    var requestUids: List<String> = emptyList()
) {

    fun toFirebaseLesson(): FirebaseLesson {
        this.lessonDate
        val day = lessonDate.day.number

        val startHourString =
            if (lessonDate.startHour.hour < 10) "0${lessonDate.startHour.hour}" else lessonDate.startHour.hour
        val endHourString =
            if (lessonDate.endHour.hour < 10) "0${lessonDate.endHour.hour}" else lessonDate.endHour.hour
        val startMinString =
            if (lessonDate.startHour.minute < 10) "0${lessonDate.startHour.minute}" else lessonDate.startHour.minute
        val endMinString =
            if (lessonDate.endHour.minute < 10) "0${lessonDate.endHour.minute}" else lessonDate.endHour.minute

        val startHour = "$startHourString.$startMinString"
        val endHour = "$endHourString.$endMinString"
        return FirebaseLesson(
            this.uid, this.name, day, startHour, endHour, this.studentUids, this.requestUids
        )
    }

}

