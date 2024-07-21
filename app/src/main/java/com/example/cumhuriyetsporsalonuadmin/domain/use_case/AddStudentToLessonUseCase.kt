package com.example.cumhuriyetsporsalonuadmin.domain.use_case

import android.util.Log
import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.domain.model.Lesson
import com.example.cumhuriyetsporsalonuadmin.domain.model.Student
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
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
                when (result) {
                    is Resource.Success -> {
                        studentList.asFlow().flatMapMerge { student ->
                            addLessonUidToStudent(lesson.uid, student.uid)
                        }.collect {
                            emit(it)
                        }
                    }

                    else -> emit(result)
                }
            }
        }

    private fun addLessonUidToStudent(
        lessonUid: String, studentUid: String
    ): Flow<Resource<in Nothing>> = flow {
        repository.getStudentByUid(studentUid).collect { result ->
            if (result !is Resource.Success) return@collect
            val student = result.data ?: return@collect
            val newList = student.lessonUids.toMutableList().apply {
                add(lessonUid)
            }
            student.lessonUids = newList
            repository.setStudent(student).collect {
                emit(it)
            }
        }
    }

}
