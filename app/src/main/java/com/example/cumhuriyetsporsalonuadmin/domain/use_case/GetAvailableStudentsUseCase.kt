package com.example.cumhuriyetsporsalonuadmin.domain.use_case

import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.domain.model.Student
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@ViewModelScoped
class GetAvailableStudentsUseCase @Inject constructor(private val repository: FirebaseRepository) {
    fun execute(lessonUid: String): Flow<Resource<List<Student>>> {
        return repository.getVerifiedStudents().flatMapConcat { result ->
            if (result !is Resource.Success) return@flatMapConcat flowOf(Resource.Error(result.message))
            val studentList = result.data?.toMutableList()
                ?: return@flatMapConcat flowOf(Resource.Error(result.message))
            val itemsToRemove = studentList.filter { it.lessonUids.contains(lessonUid) }
            studentList.removeAll(itemsToRemove)
            flowOf(Resource.Success(studentList))
        }
    }
}