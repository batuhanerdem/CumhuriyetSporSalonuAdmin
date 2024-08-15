package com.example.cumhuriyetsporsalonuadmin.domain.use_case

import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.domain.model.Lesson
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.zip
import javax.inject.Inject

@ViewModelScoped
class DeleteStudentFromLessonUseCase @Inject constructor(private val repository: FirebaseRepository) {
    fun execute(lesson: Lesson, studentUid: String): Flow<Resource<Unit>> {
        val newList = lesson.studentUids.toMutableList()
        newList.remove(studentUid)
        lesson.studentUids = newList
        return repository.removeUidFromLesson(lesson.uid, studentUid)
            .zip(repository.removeUidFromStudent(lesson.uid, studentUid)) { r1, r2 ->
                if (r1 is Resource.Success && r2 is Resource.Success) Resource.Success(Unit)
                else Resource.Error()
            }
    }
}
