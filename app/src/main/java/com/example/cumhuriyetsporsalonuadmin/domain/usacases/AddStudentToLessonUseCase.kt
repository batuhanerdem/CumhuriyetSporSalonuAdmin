package com.example.cumhuriyetsporsalonuadmin.domain.usacases

import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.domain.model.Lesson
import com.example.cumhuriyetsporsalonuadmin.domain.model.Student
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class AddStudentToLessonUseCase @Inject constructor(private val repository: FirebaseRepository) {
    fun execute(lesson: Lesson, studentList: List<Student>, callback: (Resource<Nothing>) -> Unit) {
        val newList = lesson.studentUids.toMutableList()
        studentList.map {
            newList.add(it.uid)
        }
        lesson.studentUids = newList
        repository.setLesson(lesson) { result ->
            when (result) {
                is Resource.Success -> {
                    studentList.map {
                        addLessonUidToStudent(lesson.uid, it.uid, callback)
                    }
                    callback(Resource.Success())
                }

                else -> callback
            }
        }
    }


    private fun addLessonUidToStudent(
        lessonUid: String, studentUid: String, callback: (Resource<Nothing>) -> Unit
    ) {
        repository.getStudentByUid(studentUid) { result ->
            when (result) {
                is Resource.Success -> {
                    val student = result.data ?: return@getStudentByUid
                    val newList = student.lessonUids.toMutableList()
                    newList.add(lessonUid)
                    student.lessonUids = newList
                    repository.setStudent(student) { result ->
                        when (result) {
                            is Resource.Success -> {}
                            else -> callback
                        }
                    }
                }

                else -> callback
            }
        }
    }
}