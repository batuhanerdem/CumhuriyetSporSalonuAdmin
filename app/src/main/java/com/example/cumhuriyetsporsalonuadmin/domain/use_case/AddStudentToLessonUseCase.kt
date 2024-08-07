package com.example.cumhuriyetsporsalonuadmin.domain.use_case

import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.domain.model.Lesson
import com.example.cumhuriyetsporsalonuadmin.domain.model.Student
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@ViewModelScoped
class AddStudentToLessonUseCase @Inject constructor(private val repository: FirebaseRepository) {
    @OptIn(FlowPreview::class)
    fun execute(lesson: Lesson, studentList: List<Student>): Flow<Resource<Unit>> {

        val newList = lesson.studentUids.toMutableList().apply {
            addAll(studentList.map { it.uid })
        }
        lesson.studentUids = newList

        return repository.setLesson(lesson).flatMapConcat { lessonResult ->
            if (lessonResult is Resource.Error) return@flatMapConcat flowOf(Resource.Error())

            val flowList = studentList.map { student ->
                addLessonUidToStudent(lesson.uid, student.uid)
            }

            combine(flowList) { results ->
                if (results.any { it is Resource.Error }) return@combine Resource.Error()
                Resource.Success(Unit)
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun addLessonUidToStudent(lessonUid: String, studentUid: String): Flow<Resource<Unit>> {
        return repository.getStudentByUid(studentUid).flatMapConcat { result ->
            if (result is Resource.Error) return@flatMapConcat flowOf(Resource.Error())
            val student = result.data ?: return@flatMapConcat flowOf(Resource.Error())

            val newList = student.lessonUids.toMutableList().apply {
                add(lessonUid)
            }
            student.lessonUids = newList

            repository.setStudent(student)
        }
    }
}

