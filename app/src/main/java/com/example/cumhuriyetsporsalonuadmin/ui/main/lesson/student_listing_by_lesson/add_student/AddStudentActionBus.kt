package com.example.cumhuriyetsporsalonuadmin.ui.main.lesson.student_listing_by_lesson.add_student

import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseActionBus
import com.example.cumhuriyetsporsalonuadmin.utils.Stringfy

sealed interface AddStudentActionBus : BaseActionBus {

    data object Init : AddStudentActionBus

    data class ShowError(val error: Stringfy?) : AddStudentActionBus

    data object StudentsLoaded : AddStudentActionBus

}