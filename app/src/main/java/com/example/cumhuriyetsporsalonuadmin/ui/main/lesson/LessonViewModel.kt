package com.example.cumhuriyetsporsalonuadmin.ui.main.lesson

import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.domain.model.Lesson
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseViewModel
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LessonViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : BaseViewModel<LessonActionBus>() {
    var lessonList = listOf<Lesson>()

    fun getClasses() {
        firebaseRepository.getAllLessons { result ->
            when (result) {
                is Resource.Loading -> setLoading(true)
                is Resource.Error -> sendAction(LessonActionBus.ShowError(result.message))
                is Resource.Success -> {
                    result.data?.let {
                        lessonList = it.toList()
                        sendAction(LessonActionBus.ClassesLoaded)
                    }
                }
            }

        }
    }

}