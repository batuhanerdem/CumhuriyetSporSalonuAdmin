package com.example.cumhuriyetsporsalonuadmin.ui.main.lesson.student_listing_by_lesson.add_student

import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.domain.model.LessonDate
import com.example.cumhuriyetsporsalonuadmin.domain.model.Student
import com.example.cumhuriyetsporsalonuadmin.domain.model.User
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseViewModel
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddStudentViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : BaseViewModel<AddStudentActionBus>() {

    val studentList = mutableListOf<User>()

    fun getStudents(lessonUid: String) {
        studentList.clear()
        firebaseRepository.getAllStudents() {
            studentCallback(it, lessonUid)
        }
    }

    private fun studentCallback(result: Resource<List<Student>>, lessonUid: String) {
        firebaseRepository.getLessonByUID(lessonUid) {
            it.data?.let { lesson ->
                when (result) {
                    is Resource.Error -> {
                        setLoading(false)
                        sendAction(AddStudentActionBus.ShowError(result.message))
                    }

                    is Resource.Loading -> setLoading(true)
                    is Resource.Success -> {
                        result.data?.let {
                            for (student in it) {
                                checkLessonDateConflict(student, lesson.lessonDate)
                            }
                            sendAction(AddStudentActionBus.StudentsLoaded)
                        }
                        setLoading(false)
                    }
                }
            }
        }

    }

    private fun checkLessonDateConflict(student: Student, lessonDate: LessonDate) {
        if (student.lessonUids.isEmpty()) {
            studentList.add(student)
            return
        }

        for (uid in student.lessonUids) {
            firebaseRepository.getLessonByUID(uid) {
                it.data?.let {
                    val isConflicting = LessonDate.isConflicting(it.lessonDate, lessonDate)
                    if (!isConflicting) studentList.add(student)
                }
            }
        }

    }
}