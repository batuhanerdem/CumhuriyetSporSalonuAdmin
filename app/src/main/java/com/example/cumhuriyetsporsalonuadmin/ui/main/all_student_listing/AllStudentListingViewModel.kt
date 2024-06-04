package com.example.cumhuriyetsporsalonuadmin.ui.main.all_student_listing

import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.domain.model.Lesson
import com.example.cumhuriyetsporsalonuadmin.domain.model.Student
import com.example.cumhuriyetsporsalonuadmin.domain.model.User
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseViewModel
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AllStudentListingViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : BaseViewModel<AllStudentListingActionBus>() {

    val studentList = mutableListOf<Student>()
    lateinit var lesson: Lesson


    fun getStudents() {
        studentList.clear()
        firebaseRepository.getAllStudents(::studentCallback)
    }

    private fun studentCallback(result: Resource<List<Student>>) {
        when (result) {
            is Resource.Error -> {
                setLoading(false)
                sendAction(AllStudentListingActionBus.ShowError(result.message))
            }

            is Resource.Loading -> setLoading(true)
            is Resource.Success -> {
                setLoading(false)
                result.data?.let {
                    studentList.addAll(it)
                    sendAction(AllStudentListingActionBus.StudentsLoaded)
                }
            }
        }
    }

    fun getLessonByUID(lessonUid: String) {
        firebaseRepository.getLessonByUID(lessonUid) {
            it.data?.let {
                lesson = it
                sendAction(AllStudentListingActionBus.LessonLoaded)
            }
        }
    }
}