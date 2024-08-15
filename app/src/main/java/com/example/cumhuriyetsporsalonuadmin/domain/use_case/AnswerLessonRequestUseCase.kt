package com.example.cumhuriyetsporsalonuadmin.domain.use_case

import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.domain.model.LessonRequest
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@ViewModelScoped
class AnswerLessonRequestUseCase @Inject constructor(private val repository: FirebaseRepository) {
    fun execute(lessonRequest: LessonRequest, isAccepted: Boolean): Flow<Resource<Unit>> {
        val studentUid = lessonRequest.student.uid
        val lessonUid = lessonRequest.lesson.uid

        return if (isAccepted) {
            combine(
                repository.removeUidFromRequest(lessonUid, studentUid),
                repository.addStudentUidToLesson(lessonUid, studentUid),
                repository.addLessonUidToStudent(lessonUid, studentUid)
            ) { r1, r2, r3 ->
                if (r1 is Resource.Success && r2 is Resource.Success && r3 is Resource.Success) Resource.Success(
                    Unit
                )
                else Resource.Error()
            }

        } else repository.removeUidFromRequest(lessonUid, studentUid)

    }
}