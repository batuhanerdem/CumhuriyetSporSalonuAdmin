package com.example.cumhuriyetsporsalonuadmin.domain.use_case

import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ViewModelScoped
class DeleteStudentUseCase @Inject constructor(private val repository: FirebaseRepository) {
    fun execute(studentUid: String): Flow<Resource<Unit>> = flow {
        repository.deleteStudent(studentUid).collect { result: Resource<Unit> ->
            if (result is Resource.Error) {
                emit(Resource.Error(result.message))
                return@collect
            }
            deleteStudentFromLesson(studentUid).collect {
                emit(it)
            }
        }
        return@flow
    }

    private fun deleteStudentFromLesson(studentUid: String): Flow<Resource<Unit>> = flow {
        repository.getLessonsByStudentUid(studentUid).collect { result ->

            if (result is Resource.Error) {
                emit(Resource.Error(result.message))
                return@collect
            }
            val lessonList = result.data
            if (lessonList.isNullOrEmpty()) {
                emit(Resource.Success())
                return@collect
            }

            val flowList = lessonList.map {
                val newList = it.studentUids.toMutableList()
                newList.remove(studentUid)
                it.studentUids = newList
                repository.setLesson(it)
            }
            combine(flowList) { results ->
                if (results.any { it is Resource.Error }) emit(Resource.Error())

            }.collect { emit(Resource.Success()) }
        }
        return@flow
    }
}
