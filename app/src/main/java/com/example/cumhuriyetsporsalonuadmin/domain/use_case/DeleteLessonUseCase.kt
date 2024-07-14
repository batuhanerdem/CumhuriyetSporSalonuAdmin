package com.example.cumhuriyetsporsalonuadmin.domain.use_case

import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ViewModelScoped
class DeleteLessonUseCase @Inject constructor(private val repository: FirebaseRepository) {
    suspend fun execute(lessonUid: String): Flow<Resource<in Nothing>> = flow {
        repository.deleteLesson(lessonUid).collect() { result: Resource<in Nothing> ->
            when (result) {
                is Resource.Success -> {
                    deleteLessonFromStudent(lessonUid).collect {
                        if (it is Resource.Error) emit(it)
                    }
                    emit(Resource.Success())
                }

                else -> emit(result)
            }
        }
    }

    private fun deleteLessonFromStudent(lessonUid: String): Flow<Resource<in Nothing>> = flow {
        repository.getStudentsByLessonUid(lessonUid).collect { result ->
            when (result) {
                is Resource.Success -> {
                    val studentList = result.data
                    if (studentList.isNullOrEmpty()) {
                        emit(Resource.Success())
                        return@collect
                    }
                    studentList.map {
                        val newList = it.lessonUids.toMutableList()
                        newList.remove(lessonUid)
                        it.lessonUids = newList
                        repository.setStudent(it).collect {
                            if (it is Resource.Error) emit(it)
                        }
                    }
                }

                else -> emit(result)
            }
        }
    }
}