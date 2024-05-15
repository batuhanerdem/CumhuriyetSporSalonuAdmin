package com.example.cumhuriyetsporsalonuadmin.ui.main.student_listing

import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseActionBus
import com.example.cumhuriyetsporsalonuadmin.utils.Stringfy

sealed interface StudentListingActionBus : BaseActionBus {
    data object Init : StudentListingActionBus

    data object StudentsLoaded : StudentListingActionBus

    data object LessonLoaded : StudentListingActionBus

    data class ShowError(val error: Stringfy?) : StudentListingActionBus

}