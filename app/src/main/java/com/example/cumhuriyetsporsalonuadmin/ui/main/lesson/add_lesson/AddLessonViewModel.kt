package com.example.cumhuriyetsporsalonuadmin.ui.main.lesson.add_lesson

import androidx.lifecycle.viewModelScope
import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.domain.model.Days
import com.example.cumhuriyetsporsalonuadmin.domain.model.Lesson
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseViewModel
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import com.example.cumhuriyetsporsalonuadmin.utils.SelectableData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
        selectAbleDayList.map { day ->
            if (day.isSelected) selectedList.add(day.data)
        }
        return selectedList
    }

    fun saveLesson(lesson: Lesson) {
        firebaseRepository.setLesson(lesson).onEach { result ->
            when (result) {
                is Resource.Error -> {
                    setLoading(false)
                    sendAction(AddLessonActionBus.ShowError(result.message))
                }

                is Resource.Loading -> {
                    setLoading(true)
                }

                is Resource.Success -> {
                    setLoading(false)
                    sendAction(AddLessonActionBus.Success)
                }
            }
        }.launchIn(viewModelScope)

    }

    fun clearDays() {
        selectAbleDayList.clear()
        sendAction(AddLessonActionBus.PageCleared)
    }

    fun isFirstHourBeforeSecondHour(firstHour: LocalTime, secondHour: LocalTime): Boolean {
        return firstHour.isBefore(secondHour)
    }

}