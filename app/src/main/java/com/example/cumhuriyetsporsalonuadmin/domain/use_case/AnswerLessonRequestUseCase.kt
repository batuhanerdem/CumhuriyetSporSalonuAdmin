package com.example.cumhuriyetsporsalonuadmin.domain.use_case

import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.domain.model.LessonRequest
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ViewModelScoped
class AnswerLessonRequestUseCase @Inject constructor(private val repository: FirebaseRepository) {
    fun execute(lessonRequest: LessonRequest, isAccepted: Boolean): Flow<Resource<Unit>> = flow {
        val studentUid = lessonRequest.student.uid
        val newRequestList = lessonRequest.lesson.requestUids.toMutableList().apply {
            this.remove(studentUid)
        }
        if (!isAccepted) { // delete request
            val newLesson = lessonRequest.lesson.copy(requestUids = newRequestList)
            repository.setLesson(newLesson).collect {
                emit(it)
            }
            return@flow
        }

        val newStudentList = lessonRequest.lesson.studentUids.toMutableList().apply {
            this.add(studentUid)
        }
        val newLesson = lessonRequest.lesson.copy(
            requestUids = newRequestList, studentUids = newStudentList
        )
        repository.setLesson(newLesson).collect {
            emit(it)
        }
        return@flow
    }
}