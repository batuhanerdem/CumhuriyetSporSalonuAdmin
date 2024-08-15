package com.example.cumhuriyetsporsalonuadmin.domain.use_case

import android.util.Log
import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.domain.model.Lesson
import com.example.cumhuriyetsporsalonuadmin.domain.model.LessonRequest
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@ViewModelScoped
class GetLessonRequestUseCase @Inject constructor(private val repository: FirebaseRepository) {


    @OptIn(ExperimentalCoroutinesApi::class)//for some reason
    fun execute(): Flow<Resource<List<LessonRequest>>> {
        //flatMapConcat doesn't listen changes, flatMapLatest does.
        return repository.getRequestedLessons().flatMapLatest { result ->
            val lessonRequestList = mutableListOf<LessonRequest>()
            if (result is Resource.Error) {
                return@flatMapLatest flow { emit(Resource.Error(result.message)) }
            }

            val requestedLessonList =
                result.data ?: return@flatMapLatest flow { emit(Resource.Error(result.message)) }

            val flowList = requestedLessonList.map { lesson ->
                addRequestsToListPerLesson(lesson, lesson.requestUids, lessonRequestList)
            }
            if (flowList.isEmpty()) return@flatMapLatest flowOf(Resource.Success(emptyList()))
            return@flatMapLatest combine(flowList) { list ->
                if (list.any { it is Resource.Error }) return@combine Resource.Error<List<LessonRequest>>()
                Resource.Success<List<LessonRequest>>(lessonRequestList)
            }
        }
    }

    private fun addRequestsToListPerLesson(
        lesson: Lesson, studentUids: List<String>, lessonRequestList: MutableList<LessonRequest>
    ): Flow<Resource<Unit>> {

        val flowList = studentUids.map {
            repository.getStudentByUid(it)
        }
        return combine(flowList) { result ->
            if (result.any { it is Resource.Error }) return@combine Resource.Error()
            result.forEach {
                val student = it.data ?: return@forEach
                lessonRequestList.add(LessonRequest(lesson, student))
            }
            Resource.Success(Unit)
        }
    }

}
