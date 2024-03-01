package com.example.cumhuriyetsporsalonuadmin.ui.main.lesson.add_lesson

import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.domain.model.Lesson
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseViewModel
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddLessonViewModel @Inject constructor(private val firebaseRepository: FirebaseRepository) :
    BaseViewModel<AddLessonActionBus>() {

    fun saveLesson(lesson: Lesson) {
        firebaseRepository.setLessons(lesson, ::lessonCallback)
    }

    fun deleteLesson(uid: String) {
        firebaseRepository.deleteLesson(uid, ::lessonCallback)
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