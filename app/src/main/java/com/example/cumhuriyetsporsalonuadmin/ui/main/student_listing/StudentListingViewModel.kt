package com.example.cumhuriyetsporsalonuadmin.ui.main.student_listing

import androidx.annotation.Nullable
import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.domain.model.Lesson
import com.example.cumhuriyetsporsalonuadmin.domain.model.User
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseViewModel
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StudentListingViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : BaseViewModel<StudentListingActionBus>() {

    val studentList = mutableListOf<User>()
    lateinit var lesson: Lesson


    fun getStudents(lessonUid: String?) {
        studentList.clear()
        lessonUid?.let {
            firebaseRepository.getStudentsByLesson(it, ::studentCallback)
            return
        }
        firebaseRepository.getAllStudents(::studentCallback)
    }

    private fun studentCallback(result: Resource<List<User>>) {
        when (result) {
            is Resource.Error -> {
                setLoading(false)
                sendAction(StudentListingActionBus.ShowError(result.message))
            }

            is Resource.Loading -> setLoading(true)
            is Resource.Success -> {
                setLoading(false)
                result.data?.let {
                    studentList.addAll(it)
                    sendAction(StudentListingActionBus.StudentsLoaded)
                }
            }
        }
    }

    fun getLessonByUID(lessonUid: String) {
        firebaseRepository.getLessonByUID(lessonUid) {
            it.data?.let {
                lesson = it
                sendAction(StudentListingActionBus.LessonLoaded)
            }
        }
    }
}