package com.example.cumhuriyetsporsalonuadmin.ui.main.all_student_listing

import androidx.lifecycle.viewModelScope
import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.domain.model.Lesson
import com.example.cumhuriyetsporsalonuadmin.domain.model.Student
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseViewModel
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllStudentListingViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : BaseViewModel<AllStudentListingActionBus>() {

    val studentList = mutableListOf<Student>()
    val filteredList = mutableListOf<Student>()
    lateinit var lesson: Lesson

    fun getStudents() {
        setLoading(true)
        studentList.clear()
        firebaseRepository.getVerifiedStudents().onEach { result ->
            setLoading(false)
            when (result) {
                is Resource.Error -> {
                    sendAction(AllStudentListingActionBus.ShowError(result.message))
                }

                is Resource.Success -> {
                    result.data?.let {
                        studentList.addAll(it)
                        sendAction(AllStudentListingActionBus.StudentsLoaded)
                    }
                }
            }
        }.launchIn(viewModelScope)
    }


    fun filterList(query: String?) {
        filteredList.clear()
        viewModelScope.launch {
            if (query == null) return@launch
            studentList.map {
                if (it.name.toString().lowercase()
                        .contains(query.toString().lowercase()) || it.surname.toString().lowercase()
                        .contains(query.toString().lowercase())
                ) {
                    filteredList.add(it)
                }
            }
            sendAction(AllStudentListingActionBus.StudentsFiltered)
        }
    }
}