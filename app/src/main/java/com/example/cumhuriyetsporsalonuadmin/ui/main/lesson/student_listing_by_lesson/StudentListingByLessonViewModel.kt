package com.example.cumhuriyetsporsalonuadmin.ui.main.lesson.student_listing_by_lesson

import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.domain.model.Lesson
import com.example.cumhuriyetsporsalonuadmin.domain.model.Student
import com.example.cumhuriyetsporsalonuadmin.domain.usacases.DeleteStudentFromLessonUseCase
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseViewModel
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StudentListingByLessonViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val deleteStudentFromLessonUseCase: DeleteStudentFromLessonUseCase
) : BaseViewModel<StudentListingByLessonActionBus>() {

    val studentList = mutableListOf<Student>()
    lateinit var lesson: Lesson


    fun getStudents(lessonUid: String) {
        studentList.clear()
        firebaseRepository.getStudentsByLessonUid(lessonUid) { result ->
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

    fun getLesson(lessonUid: String) {
        firebaseRepository.getLessonByUID(lessonUid) { result ->
            when (result) {
                is Resource.Error -> {
                    setLoading(false)
                    sendAction(StudentListingByLessonActionBus.ShowError(result.message))
                }

                is Resource.Loading -> setLoading(true)
                is Resource.Success -> {
                    setLoading(false)
                    result.data?.let {
                        lesson = it
                        sendAction(StudentListingByLessonActionBus.LessonLoaded)
                    }
                }
            }

        }
    }

    fun deleteStudentFromLesson(studentUid: String) {
        deleteStudentFromLessonUseCase.execute(lesson, studentUid) { result ->
            when (result) {
                is Resource.Error -> {
                    setLoading(false)
                    sendAction(StudentListingByLessonActionBus.ShowError(result.message))
                }

                is Resource.Loading -> setLoading(true)
                is Resource.Success -> {
                    setLoading(false)
                    val removedStudent: Student? = studentList.find {
                        it.uid == studentUid
                    }
                    val newList = lesson.studentUids.toMutableList()
                    newList.remove(removedStudent?.uid)
                    studentList.remove(removedStudent)
                    lesson.studentUids = newList
                    sendAction(StudentListingByLessonActionBus.StudentRemoved)
                }
            }
        }
    }
}
