package com.example.cumhuriyetsporsalonuadmin.ui.main.lesson.add_lesson

import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseActionBus
import com.example.cumhuriyetsporsalonuadmin.utils.Stringfy

sealed interface AddLessonActionBus : BaseActionBus {
    data object Init : AddLessonActionBus
    data object Loading : AddLessonActionBus
    data class ShowError(val errorMessage: Stringfy?) : AddLessonActionBus

    data object Success : AddLessonActionBus

    data object DayListGenerated : AddLessonActionBus
}