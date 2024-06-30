package com.example.cumhuriyetsporsalonuadmin.ui.main.all_student_listing.student_profile

import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseActionBus
import com.example.cumhuriyetsporsalonuadmin.ui.main.all_student_listing.AllStudentListingActionBus
import com.example.cumhuriyetsporsalonuadmin.utils.Stringfy

sealed interface StudentProfileActionBus : BaseActionBus {

    data object Init : StudentProfileActionBus

    data class ShowError(val error: Stringfy? = null) : StudentProfileActionBus

    data object StudentsLoaded : StudentProfileActionBus

}