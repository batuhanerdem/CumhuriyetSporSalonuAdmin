package com.example.cumhuriyetsporsalonuadmin.domain.use_case

import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@ViewModelScoped
class DeleteStudentUseCase @Inject constructor(private val repository: FirebaseRepository) {
    @OptIn(FlowPreview::class)
    fun execute(studentUid: String): Flow<Resource<Unit>> {
        return repository.deleteStudent(studentUid).flatMapConcat { result: Resource<Unit> ->
            if (result is Resource.Error) return@flatMapConcat flowOf(Resource.Error(result.message))
            combine(
                deleteStudentFromLesson(studentUid),
                deleteStudentFromRequests(studentUid),
                repository.clearStudentsLessons(studentUid)
            ) { r1, r2, r3 ->
                if (r1 is Resource.Success && r2 is Resource.Success && r3 is Resource.Success) Resource.Success(
                    Unit
                )
                else Resource.Error()
            }
        }
    }

    private fun deleteStudentFromLesson(studentUid: String): Flow<Resource<Unit>> {
        return repository.getLessonsByStudentUid(studentUid).flatMapConcat { result ->
            val lessonList = result.data ?: return@flatMapConcat flowOf(Resource.Success(Unit))
            if (lessonList.isEmpty()) return@flatMapConcat flowOf(Resource.Success(Unit))

            val flowList = lessonList.map {
                repository.removeUidFromLesson(it.uid, studentUid)
            }
            combine(flowList) { results ->
                if (results.any { it is Resource.Error }) Resource.Error<Unit>()
                Resource.Success(Unit)
            }
        }
    }

    private fun deleteStudentFromRequests(studentUid: String): Flow<Resource<Unit>> {
        return repository.getRequestedLessonsByStudentUid(studentUid).flatMapConcat { result ->
            val lessonList = result.data ?: return@flatMapConcat flowOf(Resource.Success(Unit))
            if (lessonList.isEmpty()) return@flatMapConcat flowOf(Resource.Success(Unit))

            val flowList = lessonList.map {
                repository.removeUidFromRequest(it.uid, studentUid)
            }
            combine(flowList) { results ->
                if (results.any { it is Resource.Error }) Resource.Error<Unit>()
                Resource.Success(Unit)
            }
        }
    }
}
