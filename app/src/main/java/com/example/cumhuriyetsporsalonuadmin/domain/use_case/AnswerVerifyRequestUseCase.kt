package com.example.cumhuriyetsporsalonuadmin.domain.use_case

import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.domain.mappers.VerifiedStatusMapper.toVerifiedStatus
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ViewModelScoped
class AnswerVerifyRequestUseCase @Inject constructor(private val repository: FirebaseRepository) {
    suspend fun execute(
        studentUid: String, answer: Boolean
    ): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        repository.getStudentByUid(studentUid).flatMapConcat { result ->
            if (result !is Resource.Success) return@flatMapConcat flow {
                if (result is Resource.Error) emit(Resource.Error())
                else emit(Resource.Loading())
            }
            val user = result.data ?: return@flatMapConcat flow { emit(Resource.Error()) }
            val answeredUser = user.copy(isVerified = answer.toVerifiedStatus())
            repository.setStudent(answeredUser)
        }.collect { emit(it) }
    }
}
