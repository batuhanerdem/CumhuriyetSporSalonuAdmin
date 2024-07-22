package com.example.cumhuriyetsporsalonuadmin.domain.use_case

import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ViewModelScoped
class DeleteLessonUseCase @Inject constructor(private val repository: FirebaseRepository) {
    suspend fun execute(lessonUid: String): Flow<Resource<Unit>> = flow {
        repository.deleteLesson(lessonUid).flatMapConcat { result: Resource<Unit> ->
            emit(Resource.Loading())
            if (result !is Resource.Success) return@flatMapConcat flow { }
            deleteLessonFromStudent(lessonUid)
        }.collect {
            emit(it)
        }

    }


    private fun deleteLessonFromStudent(lessonUid: String): Flow<Resource<Unit>> = flow {
        repository.getStudentsByLessonUid(lessonUid).flatMapConcat { result ->
            if (result !is Resource.Success) return@flatMapConcat flow { }
            val studentList = result.data
            if (studentList.isNullOrEmpty()) return@flatMapConcat flow { emit(Resource.Success()) }

            studentList.asFlow().flatMapConcat {
                val newList = it.lessonUids.toMutableList()
                newList.remove(lessonUid)
                it.lessonUids = newList
                repository.setStudent(it)
            }

        }.collect {
            emit(it)
        }
    }
}