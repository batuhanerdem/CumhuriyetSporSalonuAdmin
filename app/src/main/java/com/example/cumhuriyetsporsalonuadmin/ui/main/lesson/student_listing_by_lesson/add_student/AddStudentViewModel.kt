package com.example.cumhuriyetsporsalonuadmin.ui.main.lesson.student_listing_by_lesson.add_student

import android.util.Log
import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.domain.model.LessonDate
import com.example.cumhuriyetsporsalonuadmin.domain.model.Student
import com.example.cumhuriyetsporsalonuadmin.domain.model.User
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseViewModel
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import com.example.cumhuriyetsporsalonuadmin.utils.SelectableData
import com.example.cumhuriyetsporsalonuadmin.utils.SelectableData.Companion.toSelectable
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddStudentViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : BaseViewModel<AddStudentActionBus>() {

    var selectableStudentList = mutableListOf<SelectableData<Student>>()
    var lessonName: String = ""

    fun getStudents(lessonUid: String) {
        selectableStudentList.clear()
        firebaseRepository.getAllStudents {
            studentCallback(it, lessonUid)
        }
    }

    fun getLessonName(lessonUid: String) {
        firebaseRepository.getLessonByUID(lessonUid) { result ->
            when (result) {
                is Resource.Error -> {
                    setLoading(false)
                    sendAction(AddStudentActionBus.ShowError(result.message))
                }

                is Resource.Loading -> setLoading(true)
                is Resource.Success -> {
                    result.data?.let {
                        lessonName = it.name
                        sendAction(AddStudentActionBus.LessonNameLoaded)
                    }
                }
            }
        }
    }

    fun addStudent(lessonUid: String, studentList: List<Student>) {
        firebaseRepository.addStudentToLesson(lessonUid, studentList) { result ->
            when (result) {
                is Resource.Error -> sendAction(AddStudentActionBus.ShowError(result.message))
                is Resource.Loading -> {}
                is Resource.Success -> sendAction(AddStudentActionBus.StudentsAdded)
            }

        }
    }

    fun getSelectedStudents(): List<Student> {
        val selectedStudentList = mutableListOf<Student>()
        selectableStudentList.map {
            if (it.isSelected) selectedStudentList.add(it.data) else selectedStudentList.remove(it.data)
        }
        return selectedStudentList
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
                                checkLessonDateConflictThenAddStudentToList(
                                    student, lesson.lessonDate
                                )
                            }
                            sendAction(AddStudentActionBus.StudentsLoaded)
                        }
                        setLoading(false)
                    }
                }
            }
        }

    }

    private fun checkLessonDateConflictThenAddStudentToList(
        student: Student, lessonDate: LessonDate
    ) {
        val studentList = mutableListOf<Student>()
        if (student.lessonUids.isEmpty()) {
            studentList.add(student)
            Log.d(TAG, "checkLessonDateConflictThenAddStudentToList: test1, ${student.email}")
            selectableStudentList.addAll(studentList.toSelectable())
            return
        }

        student.lessonUids.map {
            Log.d(TAG, "checkLessonDateConflictThenAddStudentToList: test2 ${student.email}")
            firebaseRepository.getLessonByUID(it) { result ->
                result.data?.let {
                    val isConflicting = LessonDate.isConflicting(it.lessonDate, lessonDate)
                    Log.d(TAG, "checkLessonDateConflictThenAddStudentToList: ${it.lessonDate} ve $lessonDate")
                    if (!isConflicting) studentList.add(student)
                }
            }
        }
        selectableStudentList.addAll(studentList.toSelectable())
    }
}