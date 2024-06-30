package com.example.cumhuriyetsporsalonuadmin.ui.main.all_student_listing.student_profile

import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.domain.model.Student
import com.example.cumhuriyetsporsalonuadmin.domain.model.User
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseViewModel
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StudentProfileViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : BaseViewModel<StudentProfileActionBus>() {
    var user: User? = null

    fun getStudent(uid: String) {
        firebaseRepository.getStudentByUid(uid) { result ->
            when (result) {
                is Resource.Error -> {
                    setLoading(false)
                    sendAction(StudentProfileActionBus.ShowError(result.message))
                }

                is Resource.Loading -> setLoading(true)
                is Resource.Success -> {
                    setLoading(false)
                    result.data?.let {
                        user = it
                        sendAction(StudentProfileActionBus.StudentsLoaded)
                    }
                }
            }
        }
    }
}

