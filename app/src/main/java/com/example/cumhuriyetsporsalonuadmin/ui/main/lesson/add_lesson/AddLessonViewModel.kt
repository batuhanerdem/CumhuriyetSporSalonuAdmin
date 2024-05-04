package com.example.cumhuriyetsporsalonuadmin.ui.main.lesson.add_lesson

import com.example.cumhuriyetsporsalonuadmin.R
import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.domain.model.Days
import com.example.cumhuriyetsporsalonuadmin.domain.model.Lesson
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseViewModel
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import com.example.cumhuriyetsporsalonuadmin.utils.SelectableData
import com.example.cumhuriyetsporsalonuadmin.utils.Stringfy.Companion.stringfy
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class AddLessonViewModel @Inject constructor(private val firebaseRepository: FirebaseRepository) :
    BaseViewModel<AddLessonActionBus>() {
    var selectAbleDayList = mutableListOf<SelectableData<Days>>()

    fun generateDayList() {
        val dayList = mutableListOf<SelectableData<Days>>()
        for (day in enumValues<Days>().toList()) {
            dayList.add(day.toSelectable())
        }
        selectAbleDayList = dayList
        sendAction(AddLessonActionBus.DayListGenerated)
    }

    fun getSelectedDaysList(): List<Days> {
        val selectedList = mutableListOf<Days>()
        for (day in selectAbleDayList) {
            if (day.isSelected) selectedList.add(day.data)
        }
        return selectedList
    }

    fun saveLesson(lesson: Lesson) {
        val firebaseLesson = lesson.toFirebaseLesson()
        firebaseLesson?.let {
            firebaseRepository.setLessons(it, ::lessonCallback)
            return
        }
        sendAction(AddLessonActionBus.ShowError(R.string.lesson_saving_error_message.stringfy()))
    }

    fun deleteAllLessons() {
        firebaseRepository.deleteAllLessons()
    }

    fun clearDays() {
        selectAbleDayList.clear()
        sendAction(AddLessonActionBus.PageCleared)
    }

    fun isFirstHourBeforeSecondHour(firstHour: LocalTime, secondHour: LocalTime): Boolean {
        return firstHour.isBefore(secondHour)
    }

    private fun lessonCallback(result: Resource<Nothing>) {
        when (result) {
            is Resource.Error -> sendAction(AddLessonActionBus.ShowError(result.message))
            is Resource.Loading -> {}
            is Resource.Success -> {
                sendAction(AddLessonActionBus.Success)
            }
        }
    }

}