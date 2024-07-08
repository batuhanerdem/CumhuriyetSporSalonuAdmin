package com.example.cumhuriyetsporsalonuadmin.domain.use_case

import android.util.Log
import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class DeleteStudentUseCase @Inject constructor(private val repository: FirebaseRepository) {
    fun execute(studentUid: String, callback: (Resource<Nothing>) -> Unit) {
        repository.deleteStudent(studentUid) { result: Resource<Nothing> ->
            when (result) {
                is Resource.Success -> {
                    deleteStudentFromLesson(studentUid, callback)
                }

                else -> callback(result)
            }
        }
    }

    private fun deleteStudentFromLesson(studentUid: String, callback: (Resource<Nothing>) -> Unit) {
        repository.getLessonsByStudentUid(studentUid) { result ->
            when (result) {
                is Resource.Success -> {
                    val lessonList = result.data
                    if (lessonList.isNullOrEmpty()) {
                        Log.d("tag", "deleteLessonFromStudent:$lessonList ")
                        callback(Resource.Success())
                        return@getLessonsByStudentUid
                    }
                    lessonList.map {
                        val newList = it.studentUids.toMutableList()
                        newList.remove(studentUid)
                        it.studentUids = newList
                        repository.setLesson(it, callback)
                    }
                }

                else -> callback
            }
        }
    }
}