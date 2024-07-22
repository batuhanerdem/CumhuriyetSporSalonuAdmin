package com.example.cumhuriyetsporsalonuadmin.domain.use_case

import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.domain.model.Lesson
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ViewModelScoped
class DeleteStudentFromLessonUseCase @Inject constructor(private val repository: FirebaseRepository) {
    suspend fun execute(lesson: Lesson, studentUid: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        val newList = lesson.studentUids.toMutableList()
        newList.remove(studentUid)
        lesson.studentUids = newList
        repository.setLesson(lesson).flatMapLatest { result ->
            if (result !is Resource.Success) return@flatMapLatest flow { }
            deleteLessonUidFromStudent(lesson.uid, studentUid)
        }.collect {
            emit(it)
        }
    }


    private fun deleteLessonUidFromStudent(
        lessonUid: String, studentUid: String
    ): Flow<Resource<Unit>> = flow {
        repository.getStudentByUid(studentUid).flatMapLatest { result ->
            if (result !is Resource.Success) return@flatMapLatest flow { }
            val student = result.data ?: return@flatMapLatest flow { }
            val newList = student.lessonUids.toMutableList()
            newList.remove(lessonUid)
            student.lessonUids = newList
            repository.setStudent(student)
        }.collect {
            emit(it)
        }
    }
}
