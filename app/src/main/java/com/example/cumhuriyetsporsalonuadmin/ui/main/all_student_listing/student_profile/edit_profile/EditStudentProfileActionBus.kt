package com.example.cumhuriyetsporsalonuadmin.ui.main.all_student_listing.student_profile.edit_profile

import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseActionBus
import com.example.cumhuriyetsporsalonuadmin.utils.Stringfy

sealed interface EditStudentProfileActionBus : BaseActionBus {

    data object Init : EditStudentProfileActionBus

    data class ShowError(val error: Stringfy? = null) : EditStudentProfileActionBus

    data object UserUpdated : EditStudentProfileActionBus

    data object StudentLoaded : EditStudentProfileActionBus

}