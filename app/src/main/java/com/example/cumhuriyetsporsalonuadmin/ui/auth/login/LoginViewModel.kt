package com.example.cumhuriyetsporsalonuadmin.ui.auth.login

import androidx.lifecycle.viewModelScope
import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.domain.model.Admin
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseViewModel
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : BaseViewModel<LoginActionBus>() {

    fun loginForTest() {
        sendAction(LoginActionBus.LoggedIn)
    }

    fun login(admin: Admin) {
        setLoading(true)
        firebaseRepository.adminLogin(admin).onEach { result ->
            setLoading(false)
            when (result) {
                is Resource.Error -> {
                    sendAction(LoginActionBus.ShowError(result.message))
                }

                is Resource.Success -> {
                    sendAction(LoginActionBus.LoggedIn)
                }
            }
        }.launchIn(viewModelScope)

    }

}