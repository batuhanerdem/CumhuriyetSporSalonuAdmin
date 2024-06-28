package com.example.cumhuriyetsporsalonuadmin.ui.main.lesson.student_listing_by_lesson.add_student

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.domain.model.LessonDate
import com.example.cumhuriyetsporsalonuadmin.domain.model.Student
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseViewModel
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import com.example.cumhuriyetsporsalonuadmin.utils.SelectableData
import com.example.cumhuriyetsporsalonuadmin.utils.SelectableData.Companion.toSelectable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddStudentViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : BaseViewModel<AddStudentActionBus>() {

    //    var selectableStudentList = mutableListOf<SelectableData<Student>>()
    val selectableStudentList: MutableLiveData<MutableList<SelectableData<Student>>> by lazy {
        MutableLiveData<MutableList<SelectableData<Student>>>(mutableListOf())
    }
    var lessonName: String = ""
    lateinit var args: AddStudentFragmentArgs

    fun getStudents() {
        selectableStudentList.value = emptyList<SelectableData<Student>>().toMutableList()
        viewModelScope.launch {
            firebaseRepository.getAvailableStudents(args.lessonUid) { action ->
                when (action) {
                    is Resource.Error -> sendAction(AddStudentActionBus.ShowError())
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        setLoading(false)
                        val data = action.data ?: return@getAvailableStudents
                        val studentList = mutableListOf<SelectableData<Student>>()
                        data.map {
                            studentList.add(it.toSelectable())
                        }
                        selectableStudentList.value = studentList
                    }
                }
            }
        }

    }

    private fun triggerStudentListLiveData() {
        selectableStudentList.value = selectableStudentList.value
    }

    fun getLessonName() {
        firebaseRepository.getLessonByUID(args.lessonUid) { result ->
            when (result) {
                is Resource.Error -> {
                    setLoading(false)
                    sendAction(AddStudentActionBus.ShowError(result.message))
                }

                is Resource.Loading -> setLoading(true)
                is Resource.Success -> {
                    result.data?.let {
                        setLoading(false)
                        lessonName = it.name
                        sendAction(AddStudentActionBus.LessonNameLoaded)
                    }
                }
            }
        }
    }

    fun addStudent(studentList: List<Student>) {
        firebaseRepository.addStudentToLesson(args.lessonUid, studentList) { result ->
            when (result) {
                is Resource.Error -> {
                    Log.d(TAG, "addStudent: ${result.message}")
                    sendAction(AddStudentActionBus.ShowError(result.message))
                }

                is Resource.Loading -> {}
                is Resource.Success -> {
                    Log.d(TAG, "addStudent: ")
                    selectableStudentList.value =
                        emptyList<SelectableData<Student>>().toMutableList()
                    sendAction(AddStudentActionBus.StudentsAdded)
                }
            }

        }
    }

    fun getSelectedStudents(): List<Student> {
        val selectedStudentList = mutableListOf<Student>()
        selectableStudentList.value?.map {
            if (it.isSelected) selectedStudentList.add(it.data) else selectedStudentList.remove(it.data)
        }
        Log.d(TAG, "getSelectedStudents: $selectedStudentList")
        return selectedStudentList
    }


    private fun studentCallback(result: Resource<List<Student>>, lessonUid: String) {
        firebaseRepository.getLessonByUID(lessonUid) {
            it.data?.let { lesson ->
                when (result) {
                    is Resource.Error -> {
                        setLoading(false)
                        sendAction(AddStudentActionBus.ShowError(result.message))
                    }

                    is Resource.Loading -> setLoading(true)
                    is Resource.Success -> {
                        setLoading(false)
                        result.data?.let {
//                            val studentList = mutableListOf<Student>()
//                            it.map { student ->
//                                checkLessonDateConflictThenAddStudentToList(
//                                    student, lesson.lessonDate, studentList
//                                )
//                            }

                            sendAction(AddStudentActionBus.StudentsLoaded)
                        }
                        setLoading(false)
                    }
                }
            }
        }

    }

    private fun checkLessonDateConflictThenAddStudentToList(
        student: Student, lessonDate: LessonDate, studentList: MutableList<Student>
    ) {
        if (student.lessonUids.isEmpty()) {
            studentList.add(student)
            selectableStudentList.value = studentList.toSelectable()
            studentList.clear()
            return
        }

        student.lessonUids.map {
            firebaseRepository.getLessonByUID(it) { result ->
                result.data?.let {
                    val isConflicting = LessonDate.isConflicting(it.lessonDate, lessonDate)
                    if (!isConflicting) {
                        studentList.add(student)
                    }
                }
                selectableStudentList.value = studentList.toSelectable()
            }
        }
    }
}