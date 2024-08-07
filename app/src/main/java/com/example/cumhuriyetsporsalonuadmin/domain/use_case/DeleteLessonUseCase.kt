package com.example.cumhuriyetsporsalonuadmin.domain.use_case

import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@ViewModelScoped
class DeleteLessonUseCase @Inject constructor(private val repository: FirebaseRepository) {
    fun execute(lessonUid: String): Flow<Resource<Unit>> {
        return repository.deleteLesson(lessonUid).flatMapConcat { result: Resource<Unit> ->
            if (result is Resource.Error) return@flatMapConcat flowOf(Resource.Error(result.message))
            deleteLessonFromStudent(lessonUid)
        }
    }


    private fun deleteLessonFromStudent(lessonUid: String): Flow<Resource<Unit>> {
        return repository.getStudentsByLessonUid(lessonUid).flatMapConcat { result ->
            if (result is Resource.Error) return@flatMapConcat flowOf(Resource.Error(result.message))

            val studentList = result.data
            if (studentList.isNullOrEmpty()) return@flatMapConcat flowOf(Resource.Success(Unit))

            val flowList = studentList.map {
                val newList = it.lessonUids.toMutableList()
                newList.remove(lessonUid)
                it.lessonUids = newList
                repository.setStudent(it)
            }

            combine(flowList) { results ->
                if (results.any { it is Resource.Error }) Resource.Error<Unit>()
                Resource.Success(Unit)
            }
        }
    }
}