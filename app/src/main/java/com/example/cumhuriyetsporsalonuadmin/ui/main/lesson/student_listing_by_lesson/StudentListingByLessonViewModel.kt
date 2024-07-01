package com.example.cumhuriyetsporsalonuadmin.ui.main.lesson.student_listing_by_lesson

import android.util.Log
import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.domain.model.Lesson
import com.example.cumhuriyetsporsalonuadmin.domain.model.Student
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

    fun getLessonByUID(lessonUid: String) {
        firebaseRepository.getLessonByUID(lessonUid) {
            Log.d(TAG, "getLessonByUID: ${it.message}")
            it.data?.let {
                lesson = it
                sendAction(StudentListingByLessonActionBus.LessonLoaded)
            }
        }
    }

    fun deleteStudentFromLesson(studentUid: String) {
        firebaseRepository.deleteStudentFromLesson(studentUid, lesson.uid) { action ->
            when (action) {
                is Resource.Error -> {
                    setLoading(false)
                    sendAction(StudentListingByLessonActionBus.ShowError(action.message))
                }

                is Resource.Loading -> setLoading(true)
                is Resource.Success -> {
                    setLoading(false)
                    val removeList = mutableListOf<Student>()
                    studentList.map {
                        if (it.uid == studentUid) {
                            removeList.add(it)
                        }
                    }
                    val newlist = lesson.studentUids.toMutableList()
                    val removeIdList = mutableListOf<String>()
                    removeList.map {
                        removeIdList.add(it.uid)
                    }
                    newlist.removeAll(removeIdList)
                    lesson.studentUids = newlist
                    studentList.removeAll(removeList)

                    sendAction(StudentListingByLessonActionBus.StudentRemoved)
                }
            }

        }
    }

    private fun studentCallback(result: Resource<List<User>>) {
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
                    sendAction(StudentListingByLessonActionBus.StudentsLoaded)
                }
            }
        }
    }
}