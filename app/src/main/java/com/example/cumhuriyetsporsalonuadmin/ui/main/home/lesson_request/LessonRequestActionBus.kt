package com.example.cumhuriyetsporsalonuadmin.ui.main.home.lesson_request

import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseActionBus
import com.example.cumhuriyetsporsalonuadmin.utils.Stringfy

sealed interface LessonRequestActionBus : BaseActionBus {
    data object Init : LessonRequestActionBus

    data object ApplicationsLoaded : LessonRequestActionBus

    data class ShowError(val error: Stringfy? = null) : LessonRequestActionBus

    data object Answered : LessonRequestActionBus

}