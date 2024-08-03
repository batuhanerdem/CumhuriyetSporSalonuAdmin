package com.example.cumhuriyetsporsalonuadmin.domain.use_case

import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.domain.mappers.VerifiedStatusMapper.toVerifiedStatus
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ViewModelScoped
class AnswerVerifyRequestUseCase @Inject constructor(private val repository: FirebaseRepository) {
    suspend fun execute(
        studentUid: String, answer: Boolean
    ): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        repository.getStudentByUid(studentUid).collect { result ->
            if (result !is Resource.Success) {
                if (result is Resource.Error) emit(Resource.Error())
                else emit(Resource.Loading())
                return@collect
            }
            val user = result.data ?: return@collect
            val answeredUser = user.copy(isVerified = answer.toVerifiedStatus())
            repository.setStudent(answeredUser).collect {
                emit(it)
            }
        }
    }
}
