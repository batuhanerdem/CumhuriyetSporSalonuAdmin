package com.example.cumhuriyetsporsalonuadmin.ui.main.lesson.student_listing_by_lesson.add_student

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.domain.model.Lesson
import com.example.cumhuriyetsporsalonuadmin.domain.model.Student
import com.example.cumhuriyetsporsalonuadmin.domain.use_case.AddStudentToLessonUseCase
import com.example.cumhuriyetsporsalonuadmin.domain.use_case.GetAvailableStudentsUseCase
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseViewModel
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import com.example.cumhuriyetsporsalonuadmin.utils.SelectableData
import com.example.cumhuriyetsporsalonuadmin.utils.SelectableData.Companion.toSelectable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
        setLoading(true)
        firebaseRepository.getLessonByUID(args.lessonUid).onEach { result ->
            setLoading(false)
            when (result) {
                is Resource.Error -> {
                    sendAction(AddStudentActionBus.ShowError(result.message))
                }

                is Resource.Success -> {
                    result.data?.let {
                        lesson = it
                        sendAction(AddStudentActionBus.LessonLoaded)
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getStudents() {
        setLoading(true)
        selectableStudentList.value = emptyList<SelectableData<Student>>().toMutableList()
        getAvailableStudentsUseCase.execute(args.lessonUid).onEach { result ->
            setLoading(false)
            when (result) {
                is Resource.Error -> {
                    sendAction(AddStudentActionBus.ShowError(result.message))
                }

                is Resource.Success -> {
                    val studentList = mutableListOf<SelectableData<Student>>()
                    val availableStudentList = result.data ?: return@onEach
                    availableStudentList.map {
                        studentList.add(it.toSelectable())
                    }
                    selectableStudentList.value = studentList

                }
            }
        }.launchIn(viewModelScope)
    }

    fun addSelectedStudents() {
        val studentList = getSelectedStudents()
        val lessonUid = lesson?.uid ?: return
        if (studentList.isEmpty()) return
        setLoading(true)
        addStudentToLessonUseCase.execute(lessonUid, studentList).onEach { result ->
            setLoading(false)
            when (result) {
                is Resource.Error -> {
                    sendAction(AddStudentActionBus.ShowError(result.message))
                }

                is Resource.Success -> {
                    selectableStudentList.value =
                        emptyList<SelectableData<Student>>().toMutableList()
                    sendAction(AddStudentActionBus.StudentsAdded)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getSelectedStudents(): List<Student> {
        val selectedStudentList = mutableListOf<Student>()
        selectableStudentList.value?.map {
            if (it.isSelected) selectedStudentList.add(it.data) else selectedStudentList.remove(it.data)
        }
        return selectedStudentList
    }

}