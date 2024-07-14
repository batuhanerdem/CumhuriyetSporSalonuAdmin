package com.example.cumhuriyetsporsalonuadmin.ui.main.all_student_listing.lesson_listing_by_student

import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.domain.model.Lesson
import com.example.cumhuriyetsporsalonuadmin.domain.model.Student
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseViewModel
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class LessonListingByStudentViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : BaseViewModel<LessonListingByStudentActionBus>() {

    var lessonList = listOf<Lesson>()
    lateinit var student: Student

    fun getClasses(studentUid: String) {
        firebaseRepository.getLessonsByStudentUid(studentUid).onEach { result ->
            when (result) {
                is Resource.Loading -> setLoading(true)
                is Resource.Error -> {
                    setLoading(false)
                    sendAction(LessonListingByStudentActionBus.ShowError(result.message))
                }

                is Resource.Success -> {
                    result.data?.let {
                        lessonList = it.toList()
                        sendAction(LessonListingByStudentActionBus.ClassesLoaded)
                    }
                }
            }

        }.launchIn(viewModelScope)
    }

    fun getStudent(studentUid: String) {
        firebaseRepository.getStudentByUid(studentUid).onEach { action ->
            when (action) {
                is Resource.Error -> {
                    setLoading(false)
                    sendAction(LessonListingByStudentActionBus.ShowError(action.message))
                }

                is Resource.Loading -> setLoading(true)
                is Resource.Success -> {
                    setLoading(false)
                    action.data?.let {
                        student = it
                        sendAction(LessonListingByStudentActionBus.StudentLoaded)
                    }
                }
            }
        }.launchIn(viewModelScope)
    }


}