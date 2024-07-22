package com.example.cumhuriyetsporsalonuadmin.domain.use_case

import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ViewModelScoped
class DeleteStudentUseCase @Inject constructor(private val repository: FirebaseRepository) {
    suspend fun execute(studentUid: String): Flow<Resource<in Nothing>> = flow {
        repository.deleteStudent(studentUid).flatMapLatest { result: Resource<Nothing> ->
            if (result !is Resource.Success) return@flatMapLatest flow { }
            deleteStudentFromLesson(studentUid)
        }.collect {
            emit(it)
        }
    }

    private fun deleteStudentFromLesson(studentUid: String): Flow<Resource<in Nothing>> = flow {
        repository.getLessonsByStudentUid(studentUid).flatMapLatest { result ->

            if (result !is Resource.Success) return@flatMapLatest flow { }
            val lessonList = result.data
            if (lessonList.isNullOrEmpty()) {
                return@flatMapLatest flow { emit(Resource.Success()) }
            }
            lessonList.asFlow().flatMapMerge {
                val newList = it.studentUids.toMutableList()
                newList.remove(studentUid)
                it.studentUids = newList
                repository.setLesson(it)
            }
        }.collect {
            emit(it)
        }


    }
}
