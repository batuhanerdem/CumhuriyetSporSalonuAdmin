package com.example.cumhuriyetsporsalonuadmin.domain.use_case

import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ViewModelScoped
class DeleteLessonUseCase @Inject constructor(private val repository: FirebaseRepository) {
    suspend fun execute(lessonUid: String): Flow<Resource<Unit>> = flow {

        repository.deleteLesson(lessonUid).collect { result: Resource<Unit> ->
            emit(Resource.Loading())
            if (result is Resource.Error) {
                emit(result)
                return@collect
            }
            deleteLessonFromStudent(lessonUid).collect { emit(it) }
        }

    }


    private fun deleteLessonFromStudent(lessonUid: String): Flow<Resource<Unit>> = flow {
        repository.getStudentsByLessonUid(lessonUid).collect { result ->
            if (result is Resource.Error) {
                emit(Resource.Error())
                return@collect
            }
            val studentList = result.data
            if (studentList.isNullOrEmpty()) {
                emit(Resource.Success())
                return@collect
            }

            val flowList = studentList.map {
                val newList = it.lessonUids.toMutableList()
                newList.remove(lessonUid)
                it.lessonUids = newList
                repository.setStudent(it)
            }

            combine(flowList) { results ->
                if (results.any { it is Resource.Error }) {
                    emit(Resource.Error())
                }
            }.collect {
                emit(Resource.Success())
            }
        }
    }
}