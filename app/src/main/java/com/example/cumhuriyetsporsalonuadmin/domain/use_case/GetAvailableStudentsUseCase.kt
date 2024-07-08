package com.example.cumhuriyetsporsalonuadmin.domain.use_case

import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.domain.model.Student
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class GetAvailableStudentsUseCase @Inject constructor(private val repository: FirebaseRepository) {
    fun execute(lessonUid: String, callback: (Resource<List<Student>>) -> Unit) {
        repository.getVerifiedStudents { result ->
            when (result) {
                is Resource.Success -> {
                    val studentList = result.data?.toMutableList() ?: return@getVerifiedStudents
                    val itemsToRemove = studentList.filter { it.lessonUids.contains(lessonUid) }
                    studentList.removeAll(itemsToRemove)
                    callback(Resource.Success(studentList))
                }

                else -> callback
            }
        }

    }
}