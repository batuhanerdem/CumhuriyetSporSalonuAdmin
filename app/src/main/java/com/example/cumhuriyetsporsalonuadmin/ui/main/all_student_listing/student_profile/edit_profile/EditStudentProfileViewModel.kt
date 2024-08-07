package com.example.cumhuriyetsporsalonuadmin.ui.main.all_student_listing.student_profile.edit_profile

import androidx.lifecycle.viewModelScope
import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.domain.model.User
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseViewModel
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class EditStudentProfileViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : BaseViewModel<EditStudentProfileActionBus>() {
    var student: User? = null

    fun saveUser(
        name: String?, surname: String?, age: String?
    ) {
        setLoading(true)
        val currentUser = student ?: return
        val newUser = currentUser.copy(name = name, surname = surname, age = age)
        firebaseRepository.setStudent(newUser).onEach { action ->
            setLoading(false)
            when (action) {
                is Resource.Error -> sendAction(EditStudentProfileActionBus.ShowError(action.message))
                is Resource.Success -> {
                    student = newUser.copy()
                    sendAction(EditStudentProfileActionBus.UserUpdated)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getStudent(uid: String) {
        setLoading(true)
        firebaseRepository.getStudentByUid(uid).onEach { result ->
            setLoading(false)
            when (result) {
                is Resource.Error -> {
                    sendAction(EditStudentProfileActionBus.ShowError(result.message))
                }

                is Resource.Success -> {
                    result.data?.let {
                        student = it
                        sendAction(EditStudentProfileActionBus.StudentLoaded)
                    }
                }
            }
        }.launchIn(viewModelScope)
    }
}
