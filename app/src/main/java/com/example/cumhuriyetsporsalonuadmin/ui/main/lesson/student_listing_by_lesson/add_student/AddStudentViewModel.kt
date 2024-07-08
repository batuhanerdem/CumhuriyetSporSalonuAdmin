package com.example.cumhuriyetsporsalonuadmin.ui.main.lesson.student_listing_by_lesson.add_student

import androidx.lifecycle.MutableLiveData
import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.domain.model.Lesson
import com.example.cumhuriyetsporsalonuadmin.domain.model.Student
import com.example.cumhuriyetsporsalonuadmin.domain.usacases.AddStudentToLessonUseCase
import com.example.cumhuriyetsporsalonuadmin.domain.usacases.GetAvailableStudentsUseCase
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseViewModel
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import com.example.cumhuriyetsporsalonuadmin.utils.SelectableData
import com.example.cumhuriyetsporsalonuadmin.utils.SelectableData.Companion.toSelectable
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddStudentViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val getAvailableStudentsUseCase: GetAvailableStudentsUseCase,
    private val addStudentToLessonUseCase: AddStudentToLessonUseCase
) : BaseViewModel<AddStudentActionBus>() {

    val selectableStudentList: MutableLiveData<MutableList<SelectableData<Student>>> by lazy {
        MutableLiveData<MutableList<SelectableData<Student>>>(mutableListOf())
    }
    var lesson: Lesson? = null
    lateinit var args: AddStudentFragmentArgs

    fun getLesson() {
        firebaseRepository.getLessonByUID(args.lessonUid) { result ->
            when (result) {
                is Resource.Error -> {
                    setLoading(false)
                    sendAction(AddStudentActionBus.ShowError(result.message))
                }

                is Resource.Loading -> setLoading(true)
                is Resource.Success -> {
                    setLoading(false)
                    result.data?.let {
                        lesson = it
                        sendAction(AddStudentActionBus.LessonLoaded)
                    }
                }
            }
        }
    }

    fun getStudents() {
        selectableStudentList.value = emptyList<SelectableData<Student>>().toMutableList()
        getAvailableStudentsUseCase.execute(args.lessonUid) { result ->
            when (result) {
                is Resource.Error -> {
                    setLoading(false)
                    sendAction(AddStudentActionBus.ShowError(result.message))
                }

                is Resource.Loading -> setLoading(true)
                is Resource.Success -> {
                    setLoading(false)
                    val studentList = mutableListOf<SelectableData<Student>>()
                    val availableStudentList = result.data ?: return@execute
                    availableStudentList.map {
                        studentList.add(it.toSelectable())
                    }
                    selectableStudentList.value = studentList

                }
            }
        }
    }

    fun addSelectedStudents() {
        val studentList = getSelectedStudents()
        val lesson = lesson ?: return
        if (studentList.isEmpty()) return
        addStudentToLessonUseCase.execute(lesson, studentList) { result ->
            when (result) {
                is Resource.Error -> {
                    setLoading(false)
                    sendAction(AddStudentActionBus.ShowError(result.message))
                }

                is Resource.Loading -> setLoading(true)
                is Resource.Success -> {
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
        return selectedStudentList
    }

}