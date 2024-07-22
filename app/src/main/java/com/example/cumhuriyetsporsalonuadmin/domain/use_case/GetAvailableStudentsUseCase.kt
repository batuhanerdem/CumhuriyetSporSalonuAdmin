package com.example.cumhuriyetsporsalonuadmin.domain.use_case

import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.domain.model.Student
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ViewModelScoped
class GetAvailableStudentsUseCase @Inject constructor(private val repository: FirebaseRepository) {
    suspend fun execute(lessonUid: String): Flow<Resource<List<Student>>> = flow {
        repository.getVerifiedStudents().collect { result -> //collect latest yaptigimda crash yiyorum :D
            if (result !is Resource.Success) {
                emit(result)
                return@collect
            }

            val studentList = result.data?.toMutableList() ?: return@collect
            val itemsToRemove = studentList.filter { it.lessonUids.contains(lessonUid) }
            studentList.removeAll(itemsToRemove)
            emit(Resource.Success(studentList))
        }
    }
}