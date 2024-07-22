package com.example.cumhuriyetsporsalonuadmin.domain.use_case

import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.domain.model.Lesson
import com.example.cumhuriyetsporsalonuadmin.domain.model.Student
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ViewModelScoped
class AddStudentToLessonUseCase @Inject constructor(private val repository: FirebaseRepository) {
    suspend fun execute(lesson: Lesson, studentList: List<Student>): Flow<Resource<in Nothing>> =
        flow {
            emit(Resource.Loading())
            val newList = lesson.studentUids.toMutableList().apply {
                addAll(studentList.map { it.uid })
            }
            lesson.studentUids = newList
            val lessonResult = repository.setLesson(lesson)
            lessonResult.collect { result ->
                if (result !is Resource.Success) {
                    emit(result)
                    return@collect
                }
                studentList.asFlow().flatMapMerge { student ->
                    addLessonUidToStudent(lesson.uid, student.uid)
                }.collect {
                    emit(it)
                }

            }
        }

    private fun addLessonUidToStudent(
        lessonUid: String, studentUid: String
    ): Flow<Resource<in Nothing>> = flow {
        repository.getStudentByUid(studentUid).flatMapLatest { result ->
            if (result !is Resource.Success) return@flatMapLatest flow { }
            val student = result.data ?: return@flatMapLatest flow { }
            val newList = student.lessonUids.toMutableList().apply {
                add(lessonUid)
            }
            student.lessonUids = newList
            repository.setStudent(student)
        }.collect {
            emit(it)
        }
    }

}
