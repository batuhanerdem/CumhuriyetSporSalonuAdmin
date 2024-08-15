package com.example.cumhuriyetsporsalonuadmin.domain.use_case

import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.domain.model.Student
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.zip
import javax.inject.Inject

@ViewModelScoped
class AddStudentToLessonUseCase @Inject constructor(private val repository: FirebaseRepository) {
    fun execute(lessonUid: String, studentList: List<Student>): Flow<Resource<Unit>> {
        val flowList = studentList.map {
            repository.addStudentUidToLesson(lessonUid, it.uid)
                .zip(repository.addLessonUidToStudent(lessonUid, it.uid)) { r1, r2 ->
                    if (r1 is Resource.Success && r2 is Resource.Success) Resource.Success(Unit)
                    else Resource.Error<Unit>()
                }
        }
        return combine(flowList) { list ->
            if (list.any { it is Resource.Error }) Resource.Error<Unit>()
            Resource.Success(Unit)
        }
    }
}

