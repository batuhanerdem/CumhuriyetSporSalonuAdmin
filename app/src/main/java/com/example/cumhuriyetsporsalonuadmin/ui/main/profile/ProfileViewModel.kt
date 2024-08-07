package com.example.cumhuriyetsporsalonuadmin.ui.main.profile

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
class ProfileViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : BaseViewModel<ProfileActionBus>() {

    fun updateAdmin(admin: Admin) {
        setLoading(true)
        firebaseRepository.updateAdmin(admin).onEach {
            setLoading(false)
            when (it) {
                is Resource.Error -> {
                    sendAction(ProfileActionBus.ShowError(it.message))
                }

                is Resource.Success -> {
                    sendAction(ProfileActionBus.UpdatedSuccessFully)
                }
            }
        }.launchIn(viewModelScope)

    }
}