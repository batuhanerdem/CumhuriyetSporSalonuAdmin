package com.example.cumhuriyetsporsalonuadmin.ui.main.lesson

import com.example.cumhuriyetsporsalonuadmin.domain.model.Lesson
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseActionBus
import com.example.cumhuriyetsporsalonuadmin.utils.Stringfy

sealed interface LessonActionBus : BaseActionBus {

    data object Init : LessonActionBus

    data class ShowError(val error: Stringfy?) : LessonActionBus

    data class ClassesLoaded(val lessonList: List<Lesson>) : LessonActionBus

    data object Loading : LessonActionBus
}