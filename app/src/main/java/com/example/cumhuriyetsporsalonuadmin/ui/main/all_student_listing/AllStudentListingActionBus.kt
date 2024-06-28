package com.example.cumhuriyetsporsalonuadmin.ui.main.all_student_listing

import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseActionBus
import com.example.cumhuriyetsporsalonuadmin.utils.Stringfy

sealed interface AllStudentListingActionBus : BaseActionBus {
    data object Init : AllStudentListingActionBus

    data object StudentsLoaded : AllStudentListingActionBus

    data object LessonLoaded : AllStudentListingActionBus

    data class ShowError(val error: Stringfy?) : AllStudentListingActionBus

    data object StudentsFiltered : AllStudentListingActionBus

}