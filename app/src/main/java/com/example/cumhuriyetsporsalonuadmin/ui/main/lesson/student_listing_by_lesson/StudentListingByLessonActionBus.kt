package com.example.cumhuriyetsporsalonuadmin.ui.main.lesson.student_listing_by_lesson

import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseActionBus
import com.example.cumhuriyetsporsalonuadmin.utils.Stringfy

sealed interface StudentListingByLessonActionBus : BaseActionBus {
    data object Init : StudentListingByLessonActionBus

    data object StudentsLoaded : StudentListingByLessonActionBus

    data object LessonLoaded : StudentListingByLessonActionBus

    data class ShowError(val error: Stringfy?) : StudentListingByLessonActionBus

}