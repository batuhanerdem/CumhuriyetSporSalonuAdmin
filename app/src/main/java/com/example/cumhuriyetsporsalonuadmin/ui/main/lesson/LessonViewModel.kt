package com.example.cumhuriyetsporsalonuadmin.ui.main.lesson

import android.util.Log
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
    var lessonList = mutableListOf<Lesson>()
    var deletedLessonName = ""

    fun getClasses() {
        firebaseRepository.getAllLessons { result ->
            when (result) {
                is Resource.Loading -> setLoading(true)
                is Resource.Error -> sendAction(LessonActionBus.ShowError(result.message))
                is Resource.Success -> {
                    result.data?.let {
                        lessonList = it.toMutableList()
                        setLoading(false)
                        sendAction(LessonActionBus.ClassesLoaded)
                    }
                }
            }

        }
    }

    fun deleteLesson(lessonUid: String) {
        firebaseRepository.deleteLesson(lessonUid) { action ->
            when (action) {
                is Resource.Error -> {
                    setLoading(false)
                    sendAction(LessonActionBus.ShowError(action.message))
                }

                is Resource.Loading -> {}
                is Resource.Success -> {
                    setLoading(false)
                    Log.d(TAG, "UnDeletedList: $lessonList")

                    deletedLessonName = action.data ?: ""
                    lessonList.removeIf {
                        it.uid == lessonUid
                    }
                    Log.d(TAG, "deletedList: $lessonList")
                    sendAction(LessonActionBus.LessonDeleted)
                }
            }

        }
    }

}