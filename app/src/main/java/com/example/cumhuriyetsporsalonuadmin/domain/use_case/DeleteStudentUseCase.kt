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
class DeleteStudentUseCase @Inject constructor(private val repository: FirebaseRepository) {
    fun execute(studentUid: String): Flow<Resource<Unit>> {
        return repository.deleteStudent(studentUid).flatMapConcat { result: Resource<Unit> ->
            if (result is Resource.Error) return@flatMapConcat flowOf(Resource.Error(result.message))
            deleteStudentFromLesson(studentUid)
        }
    }

    private fun deleteStudentFromLesson(studentUid: String): Flow<Resource<Unit>> {
        return repository.getLessonsByStudentUid(studentUid).flatMapConcat { result ->

            if (result is Resource.Error) return@flatMapConcat flowOf(Resource.Error(result.message))
            val lessonList = result.data
            if (lessonList.isNullOrEmpty()) return@flatMapConcat flowOf(Resource.Success(Unit))

            val flowList = lessonList.map {
                val newList = it.studentUids.toMutableList()
                newList.remove(studentUid)
                it.studentUids = newList
                repository.setLesson(it)
            }
            combine(flowList) { results ->
                if (results.any { it is Resource.Error }) Resource.Error<Unit>()
                Resource.Success(Unit)
            }
        }
    }
}
