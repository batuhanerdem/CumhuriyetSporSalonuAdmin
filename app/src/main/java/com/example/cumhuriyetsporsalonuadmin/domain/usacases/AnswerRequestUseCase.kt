package com.example.cumhuriyetsporsalonuadmin.domain.usacases

import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.domain.mappers.VerifiedStatusMapper.toVerifiedStatus
import com.example.cumhuriyetsporsalonuadmin.domain.model.Student
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class AnswerRequestUseCase @Inject constructor(private val repository: FirebaseRepository) {
    fun execute(studentUid: String, answer: Boolean, callback: (Resource<out Student>) -> Unit) {
        callback(Resource.Loading())
        repository.getStudentByUid(studentUid) {
            when (it) {
                is Resource.Success -> {
                    val user = it.data ?: return@getStudentByUid
                    val answeredUser = user.copy(isVerified = answer.toVerifiedStatus())
                    repository.setStudent(answeredUser, callback)
                }

                else -> {
                    callback(it)
                }
            }

        }
    }
}