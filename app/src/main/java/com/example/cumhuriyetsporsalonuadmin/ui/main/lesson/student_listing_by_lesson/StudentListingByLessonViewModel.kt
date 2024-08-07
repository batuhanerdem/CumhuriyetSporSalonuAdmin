package com.example.cumhuriyetsporsalonuadmin.ui.main.lesson.student_listing_by_lesson

import androidx.lifecycle.viewModelScope
import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.domain.model.Lesson
import com.example.cumhuriyetsporsalonuadmin.domain.model.Student
import com.example.cumhuriyetsporsalonuadmin.domain.use_case.DeleteStudentFromLessonUseCase
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseViewModel
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class StudentListingByLessonViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val deleteStudentFromLessonUseCase: DeleteStudentFromLessonUseCase
) : BaseViewModel<StudentListingByLessonActionBus>() {

    val studentList = mutableListOf<Student>()
    lateinit var lesson: Lesson


    fun getStudents(lessonUid: String) {
        setLoading(true)
        studentList.clear()
        firebaseRepository.getStudentsByLessonUid(lessonUid).onEach { result ->
            setLoading(false)
            when (result) {
                is Resource.Error -> {
                    sendAction(StudentListingByLessonActionBus.ShowError(result.message))
                }

                is Resource.Success -> {
                    result.data?.let {
                        studentList.addAll(it)
                        sendAction(StudentListingByLessonActionBus.StudentsLoaded)
                    }
                }
            }

        }.launchIn(viewModelScope)
    }

    fun getLesson(lessonUid: String) {
        setLoading(true)
        firebaseRepository.getLessonByUID(lessonUid).onEach { result ->
            setLoading(false)
            when (result) {
                is Resource.Error -> {
                    sendAction(StudentListingByLessonActionBus.ShowError(result.message))
                }

                is Resource.Success -> {
                    result.data?.let {
                        lesson = it
                        sendAction(StudentListingByLessonActionBus.LessonLoaded)
                    }
                }
            }

        }.launchIn(viewModelScope)
    }

    fun deleteStudentFromLesson(studentUid: String) {
        setLoading(true)
        deleteStudentFromLessonUseCase.execute(lesson, studentUid).onEach { result ->
            setLoading(false)
            when (result) {
                is Resource.Error -> {
                    sendAction(StudentListingByLessonActionBus.ShowError(result.message))
                }

                is Resource.Success -> {
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
        }.launchIn(viewModelScope)
    }
}
