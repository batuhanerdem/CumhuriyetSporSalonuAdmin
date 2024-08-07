package com.example.cumhuriyetsporsalonuadmin.domain.use_case

import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.domain.model.Lesson
import com.example.cumhuriyetsporsalonuadmin.domain.model.LessonRequest
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ViewModelScoped
class GetLessonRequestUseCase @Inject constructor(private val repository: FirebaseRepository) {
    private val lessonRequestList = mutableListOf<LessonRequest>()

    fun execute(): Flow<Resource<List<LessonRequest>>> = flow {
        repository.getRequestedLessons().collect { result ->
            if (result is Resource.Error) {
                emit(Resource.Error(result.message))
                return@collect
            }

            val requestedLessonList = result.data ?: return@collect

            val flowList = requestedLessonList.map { lesson ->
                addRequestsToListPerLesson(lesson, lesson.requestUids)
            }
            combine(flowList) { list ->
                if (list.any { it is Resource.Error }) return@combine

            }.collect {
                emit(Resource.Success(lessonRequestList))
            }
        }
        return@flow
    }

    private suspend fun addRequestsToListPerLesson(
        lesson: Lesson, studentUids: List<String>
    ): Flow<Resource<Unit>> = flow {

        val flowList = studentUids.map {
            repository.getStudentByUid(it)
        }
        combine(flowList) { result ->
            if (result.any { it is Resource.Error }) return@combine

            result.forEach {
                val student = it.data ?: return@forEach
                lessonRequestList.add(LessonRequest(lesson, student))
            }

        }.collect {
            emit(Resource.Success(Unit))
        }
        return@flow
    }

}
