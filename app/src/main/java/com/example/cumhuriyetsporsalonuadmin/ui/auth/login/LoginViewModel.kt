package com.example.cumhuriyetsporsalonuadmin.ui.auth.login

import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.domain.model.Admin
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseViewModel
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : BaseViewModel<LoginActionBus>() {

    fun loginForTest() {
        sendAction(LoginActionBus.LoggedIn)
    }

    fun loginWithEmailAndPassword(admin: Admin) {
        sendAction(LoginActionBus.Loading)
        firebaseRepository.adminLogin(admin) { result ->
            when (result) {
                is Resource.Loading -> {}
                is Resource.Error -> sendAction(LoginActionBus.ShowError(result.message))
                is Resource.Success -> sendAction(LoginActionBus.LoggedIn)
            }
        }

    }

}