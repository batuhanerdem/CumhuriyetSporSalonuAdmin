package com.example.cumhuriyetsporsalonuadmin.domain.use_case

import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.domain.model.Lesson
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ViewModelScoped
class DeleteStudentFromLessonUseCase @Inject constructor(private val repository: FirebaseRepository) {
    suspend fun execute(lesson: Lesson, studentUid: String): Flow<Resource<in Nothing>> = flow {
        emit(Resource.Loading())
        val newList = lesson.studentUids.toMutableList()
        newList.remove(studentUid)
        lesson.studentUids = newList
        repository.setLesson(lesson).collect { result ->
            when (result) {
                is Resource.Success -> {
                    deleteLessonUidFromStudent(lesson.uid, studentUid).collect { result ->
                        if (result is Resource.Error) {
                            emit(result)
                        }
                    }
                    emit(Resource.Success())
                }

                else -> emit(result)
            }
        }
    }


    private fun deleteLessonUidFromStudent(
        lessonUid: String, studentUid: String
    ): Flow<Resource<in Nothing>> = flow {
        repository.getStudentByUid(studentUid).collect { result ->
            when (result) {
                is Resource.Success -> {
                    val student = result.data ?: return@collect
                    val newList = student.lessonUids.toMutableList()
                    newList.remove(lessonUid)
                    student.lessonUids = newList
                    repository.setStudent(student).collect {
                        if (it is Resource.Error) {
                            emit(it)
                        }
                    }
                }

                else -> emit(result)
            }
        }
    }
}