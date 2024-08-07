package com.example.cumhuriyetsporsalonuadmin.domain.use_case

import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.domain.model.Lesson
import com.example.cumhuriyetsporsalonuadmin.domain.model.Student
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ViewModelScoped
class AddStudentToLessonUseCase @Inject constructor(private val repository: FirebaseRepository) {
    fun execute(lesson: Lesson, studentList: List<Student>): Flow<Resource<Unit>> = flow {

        val newList = lesson.studentUids.toMutableList().apply {
            addAll(studentList.map { it.uid })
        }
        lesson.studentUids = newList

        repository.setLesson(lesson).collect { lessonResult ->
            if (lessonResult is Resource.Error) {
                emit(lessonResult)
                return@collect
            }

            val flowList = studentList.map { student ->
                addLessonUidToStudent(lesson.uid, student.uid)
            }

            combine(flowList) { results ->
                if (results.any { it is Resource.Error }) {
                    emit(Resource.Error())
                }
            }.collect {
                emit(Resource.Success(Unit))
            }
        }
        return@flow
    }

    private fun addLessonUidToStudent(lessonUid: String, studentUid: String): Flow<Resource<Unit>> =
        flow {
            repository.getStudentByUid(studentUid).collect { result ->
                if (result is Resource.Error) {
                    emit(Resource.Error())
                    return@collect
                }
                val student = result.data ?: return@collect

                val newList = student.lessonUids.toMutableList().apply {
                    add(lessonUid)
                }
                student.lessonUids = newList

                repository.setStudent(student).collect { emit(it) }
            }
        }
}

