package com.example.cumhuriyetsporsalonuadmin.ui.main.lesson.student_listing_by_lesson

import android.util.Log
import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.domain.model.Lesson
import com.example.cumhuriyetsporsalonuadmin.domain.model.User
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseViewModel
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StudentListingByLessonViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : BaseViewModel<StudentListingByLessonActionBus>() {

    val studentList = mutableListOf<User>()
    lateinit var lesson: Lesson


    fun getStudents(lessonUid: String) {
        studentList.clear()
        firebaseRepository.getStudentsByLesson(lessonUid, ::studentCallback)
    }

    private fun studentCallback(result: Resource<List<User>>) {
        Log.d(TAG, "studentCallback: t")
        when (result) {
            is Resource.Error -> {
                setLoading(false)
                sendAction(StudentListingByLessonActionBus.ShowError(result.message))
            }

            is Resource.Loading -> setLoading(true)
            is Resource.Success -> {
                setLoading(false)
                result.data?.let {
                    studentList.addAll(it)
                    Log.d(TAG, "studentCallback: $studentList")
                    sendAction(StudentListingByLessonActionBus.StudentsLoaded)
                }
            }
        }
    }

    fun getLessonByUID(lessonUid: String) {
        firebaseRepository.getLessonByUID(lessonUid) {
            it.data?.let {
                lesson = it
                sendAction(StudentListingByLessonActionBus.LessonLoaded)
            }
        }
    }
}