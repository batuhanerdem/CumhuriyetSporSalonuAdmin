package com.example.cumhuriyetsporsalonuadmin.domain.use_case

import android.util.Log
import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.domain.model.Lesson
import com.example.cumhuriyetsporsalonuadmin.domain.model.LessonRequest
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ViewModelScoped
class GetLessonRequestUseCase @Inject constructor(private val repository: FirebaseRepository) {

    suspend fun execute(): Flow<Resource<List<LessonRequest>>> = flow {
//        emit(Resource.Loading())
        repository.getRequestedLessons().collect { result ->
            when (result) {
                is Resource.Error -> emit(Resource.Error(result.message))
                is Resource.Loading -> emit(Resource.Loading())
                is Resource.Success -> {

                    result.data?.let { requestedLessonList ->

                        val lessonRequestList = mutableListOf<LessonRequest>()
                        requestedLessonList.forEach() { lesson ->
                            Log.d("tag", "execute: lessonlsit $requestedLessonList")
                            createLessonRequestList(lessonRequestList, lesson)
                        }
                        emit(Resource.Success(lessonRequestList))
                        Log.d("tag", "execute: emitledim ins")

                    }
                }
            }

        }
    }

    private suspend fun createLessonRequestList(list: MutableList<LessonRequest>, lesson: Lesson) {
        lesson.requestUids.asFlow().flatMapMerge {
            repository.getStudentByUid(it)
        }.collect {
            Log.d(TAG, "createLessonRequestList: $lesson -  ${it.data}")
            it.data?.let {
                list.add(LessonRequest(lesson, it))
            }
        }

    }

}

const val TAG = "tag"
