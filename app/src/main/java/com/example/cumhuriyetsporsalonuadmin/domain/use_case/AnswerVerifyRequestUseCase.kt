package com.example.cumhuriyetsporsalonuadmin.domain.use_case

import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.domain.mappers.VerifiedStatusMapper.toVerifiedStatus
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@ViewModelScoped
class AnswerVerifyRequestUseCase @Inject constructor(private val repository: FirebaseRepository) {
    fun execute(
        studentUid: String, answer: Boolean
    ): Flow<Resource<Unit>> {
        return repository.getStudentByUid(studentUid).flatMapConcat { result ->
            if (result is Resource.Error) return@flatMapConcat flowOf(Resource.Error(result.message))
            val user = result.data ?: return@flatMapConcat flowOf(Resource.Error(result.message))
            val answeredUser = user.copy(isVerified = answer.toVerifiedStatus())
            repository.setStudent(answeredUser)
        }
    }
}

