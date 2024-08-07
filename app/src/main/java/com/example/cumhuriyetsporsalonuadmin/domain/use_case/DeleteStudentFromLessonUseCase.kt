package com.example.cumhuriyetsporsalonuadmin.domain.use_case

import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.domain.model.Lesson
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@ViewModelScoped
class DeleteStudentFromLessonUseCase @Inject constructor(private val repository: FirebaseRepository) {
    fun execute(lesson: Lesson, studentUid: String): Flow<Resource<Unit>> {
        val newList = lesson.studentUids.toMutableList()
        newList.remove(studentUid)
        lesson.studentUids = newList

        return repository.setLesson(lesson).flatMapConcat { result ->
            if (result is Resource.Error) return@flatMapConcat flowOf(Resource.Error(result.message))
            deleteLessonUidFromStudent(lesson.uid, studentUid)
        }
    }


    private fun deleteLessonUidFromStudent(
        lessonUid: String, studentUid: String
    ): Flow<Resource<Unit>> {
        return repository.getStudentByUid(studentUid).flatMapConcat { result ->
            if (result is Resource.Error) return@flatMapConcat flowOf(Resource.Error(result.message))
            val student = result.data ?: return@flatMapConcat flowOf(Resource.Error(result.message))
            val newList = student.lessonUids.toMutableList()
            newList.remove(lessonUid)
            student.lessonUids = newList
            repository.setStudent(student)
        }
    }
}
