package com.example.cumhuriyetsporsalonuadmin.ui.main.all_student_listing.student_profile.edit_profile

import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.domain.model.User
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseViewModel
import com.example.cumhuriyetsporsalonuadmin.ui.main.all_student_listing.student_profile.StudentProfileActionBus
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditStudentProfileViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : BaseViewModel<EditStudentProfileActionBus>() {
    var user: User? = null

    fun saveUser(
        name: String?, surname: String?, age: String?
    ) {
        val currentUser = user ?: return
        val newUser = currentUser.copy(name = name, surname = surname, age = age)
        firebaseRepository.setUser(newUser) { action ->
            when (action) {
                is Resource.Error -> sendAction(EditStudentProfileActionBus.ShowError(action.message))
                is Resource.Loading -> {}
                is Resource.Success -> {
                    user = newUser.copy()
                    sendAction(EditStudentProfileActionBus.UserUpdated)
                }
            }
        }
    }

    fun getStudent(uid: String) {
        firebaseRepository.getStudentByUid(uid) { result ->
            when (result) {
                is Resource.Error -> {
                    setLoading(false)
                    sendAction(EditStudentProfileActionBus.ShowError(result.message))
                }

                is Resource.Loading -> setLoading(true)
                is Resource.Success -> {
                    setLoading(false)
                    result.data?.let {
                        user = it
                        sendAction(EditStudentProfileActionBus.StudentLoaded)
                    }
                }
            }
        }
    }
}
