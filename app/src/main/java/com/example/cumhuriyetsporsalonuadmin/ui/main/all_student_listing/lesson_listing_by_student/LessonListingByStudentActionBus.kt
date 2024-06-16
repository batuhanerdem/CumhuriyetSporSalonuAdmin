package com.example.cumhuriyetsporsalonuadmin.ui.main.all_student_listing.lesson_listing_by_student

import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseActionBus
import com.example.cumhuriyetsporsalonuadmin.utils.Stringfy

sealed interface LessonListingByStudentActionBus : BaseActionBus {

    data object Init : LessonListingByStudentActionBus

    data class ShowError(val error: Stringfy?) : LessonListingByStudentActionBus

    data object ClassesLoaded : LessonListingByStudentActionBus

    data object StudentLoaded : LessonListingByStudentActionBus

}