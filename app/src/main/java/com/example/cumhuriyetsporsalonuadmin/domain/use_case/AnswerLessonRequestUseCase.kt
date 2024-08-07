package com.example.cumhuriyetsporsalonuadmin.domain.use_case

import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.domain.model.LessonRequest
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class AnswerLessonRequestUseCase @Inject constructor(private val repository: FirebaseRepository) {
    fun execute(lessonRequest: LessonRequest, isAccepted: Boolean): Flow<Resource<Unit>> {
        val studentUid = lessonRequest.student.uid
        val newRequestList = lessonRequest.lesson.requestUids.toMutableList().apply {
            this.remove(studentUid)
        }
        val newLesson = if (!isAccepted) {
            lessonRequest.lesson.copy(requestUids = newRequestList)
        } else {
            val newStudentList = lessonRequest.lesson.studentUids.toMutableList().apply {
                this.add(studentUid)
            }
            lessonRequest.lesson.copy(
                requestUids = newRequestList, studentUids = newStudentList
            )
        }
        return repository.setLesson(newLesson)
    }
}