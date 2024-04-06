package com.example.cumhuriyetsporsalonuadmin.ui.main.home

import com.example.cumhuriyetsporsalonuadmin.domain.model.User
import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeVIewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : BaseViewModel<HomeActionBus>() {
    var unverifiedList = listOf<User>()

    fun getUnverifiedUsers() {
        firebaseRepository.getUnverifiedUsers {
            unverifiedList = it
            sendAction(HomeActionBus.Success)
        }
    }

    fun answerRequest(user: User, isAccepted: Boolean) {
        firebaseRepository.answerRequest(user, isAccepted) {
            sendAction(HomeActionBus.Accepted)
        }
    }


}
