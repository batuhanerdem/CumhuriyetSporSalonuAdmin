package com.example.cumhuriyetsporsalonuadmin.domain.use_case

import android.util.Log
import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ViewModelScoped
class DeleteStudentUseCase @Inject constructor(private val repository: FirebaseRepository) {
    suspend fun execute(studentUid: String): Flow<Resource<in Nothing>> = flow {
        repository.deleteStudent(studentUid).collect { result: Resource<Nothing> ->
            when (result) {
                is Resource.Success -> {
                    deleteStudentFromLesson(studentUid).collect {
                        emit(it)
                    }
                }

                else -> emit(result)
            }
        }
    }

    private fun deleteStudentFromLesson(studentUid: String): Flow<Resource<in Nothing>> = flow {
        repository.getLessonsByStudentUid(studentUid).collect { result ->
            when (result) {
                is Resource.Success -> {
                    val lessonList = result.data
                    if (lessonList.isNullOrEmpty()) {
                        emit(Resource.Success())
                        return@collect
                    }
                    lessonList.map {
                        val newList = it.studentUids.toMutableList()
                        newList.remove(studentUid)
                        it.studentUids = newList
                        repository.setLesson(it).collect { if (it is Resource.Error) emit(it) }
                    }
                }

                else -> emit(result)
            }
        }
    }
}