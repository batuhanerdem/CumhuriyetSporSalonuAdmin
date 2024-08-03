package com.example.cumhuriyetsporsalonuadmin.domain.use_case

import android.util.Log
import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.domain.model.Lesson
import com.example.cumhuriyetsporsalonuadmin.domain.model.Student
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ViewModelScoped
class AddStudentToLessonUseCase @Inject constructor(private val repository: FirebaseRepository) {
     fun execute(lesson: Lesson, studentList: List<Student>): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        val newList = lesson.studentUids.toMutableList().apply {
            addAll(studentList.map { it.uid })
        }
        lesson.studentUids = newList
        repository.setLesson(lesson).collect {
            if (it is Resource.Error) emit(it)
            Log.d(TAG, "execute: vay anasini")
        }
        Log.d(TAG, "execute: setlesson bitti(umarim)")
        val flowList = mutableListOf<Flow<Resource<Unit>>>()
        repeat(studentList.count()) { int ->
            val studentUid = studentList[int].uid
            flowList.add(addLessonUidToStudent(lesson.uid, studentUid))
        }
        combine(flowList) { list ->
            Log.d(TAG, "execute: combine calisti")
            list.forEach {
                if (it.message != null) return@combine Resource.Error()
            }
            return@combine Resource.Success<Unit>()
        }.collect {
            Log.d(TAG, "execute: emitledi $it")
            emit(it)
        }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    private fun addLessonUidToStudent(
        lessonUid: String, studentUid: String
    ): Flow<Resource<Unit>> = flow {
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
