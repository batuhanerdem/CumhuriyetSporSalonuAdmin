package com.example.cumhuriyetsporsalonuadmin.domain.use_case

import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class DeleteLessonUseCase @Inject constructor(private val repository: FirebaseRepository) {
    fun execute(lessonUid: String, callback: (Resource<Nothing>) -> Unit) {
        repository.deleteLesson(lessonUid) { result: Resource<Nothing> ->
            when (result) {
                is Resource.Success -> {
                    deleteLessonFromStudent(lessonUid, callback)
                }

                else -> callback(result)
            }
        }
    }

    private fun deleteLessonFromStudent(lessonUid: String, callback: (Resource<Nothing>) -> Unit) {
        repository.getStudentsByLessonUid(lessonUid) { result ->
            when (result) {
                is Resource.Success -> {
                    val studentList = result.data
                    if (studentList.isNullOrEmpty()) {
                        callback(Resource.Success())
                        return@getStudentsByLessonUid
                    }
                    studentList.map {
                        val newList = it.lessonUids.toMutableList()
                        newList.remove(lessonUid)
                        it.lessonUids = newList
                        repository.setStudent(it, callback)
                    }
                }

                is Resource.Error -> callback(Resource.Error(result.message))
                is Resource.Loading -> callback(Resource.Loading())
            }
        }
    }
}