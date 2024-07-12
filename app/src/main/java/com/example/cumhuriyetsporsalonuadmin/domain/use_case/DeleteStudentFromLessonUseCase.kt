package com.example.cumhuriyetsporsalonuadmin.domain.use_case

import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.domain.model.Lesson
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class DeleteStudentFromLessonUseCase @Inject constructor(private val repository: FirebaseRepository) {
    fun execute(lesson: Lesson, studentUid: String, callback: (Resource<Nothing>) -> Unit) {
        val newList = lesson.studentUids.toMutableList()
        newList.remove(studentUid)
        lesson.studentUids = newList
        repository.setLesson(lesson) { result ->
            when (result) {
                is Resource.Success -> {
                    deleteLessonUidFromStudent(lesson.uid, studentUid, callback)
                }

                else -> callback(result)
            }
        }
    }


    private fun deleteLessonUidFromStudent(
        lessonUid: String, studentUid: String, callback: (Resource<Nothing>) -> Unit
    ) {
        repository.getStudentByUid(studentUid) { result ->
            when (result) {
                is Resource.Success -> {
                    val student = result.data ?: return@getStudentByUid
                    val newList = student.lessonUids.toMutableList()
                    newList.remove(lessonUid)
                    student.lessonUids = newList
                    repository.setStudent(student, callback)
                }

                is Resource.Error -> callback(Resource.Error(result.message))
                is Resource.Loading -> callback(Resource.Loading())
            }
        }
    }
}