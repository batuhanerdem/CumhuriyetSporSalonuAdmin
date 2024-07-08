package com.example.cumhuriyetsporsalonuadmin.ui.main.home

import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.domain.model.User
import com.example.cumhuriyetsporsalonuadmin.domain.usacases.AnswerRequestUseCase
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseViewModel
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeVIewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val answerRequestUseCase: AnswerRequestUseCase
) : BaseViewModel<HomeActionBus>() {
    var unverifiedList = listOf<User>()

    fun getUnverifiedUsers() {
        firebaseRepository.getUnverifiedStudents { result ->
            when (result) {
                is Resource.Error -> {
                    setLoading(false)
                    sendAction(HomeActionBus.ShowError(result.message))
                }

                is Resource.Loading -> setLoading(true)
                is Resource.Success -> {
                    setLoading(false)
                    result.data?.let { list ->
                        unverifiedList = list
                        sendAction(HomeActionBus.ApplicationsLoaded)
                    }
                }
            }
        }
    }

    fun answerRequest(user: User, isAccepted: Boolean) {
        answerRequestUseCase.execute(user.uid, isAccepted) { result ->
            when (result) {
                is Resource.Error -> {
                    setLoading(false)
                    sendAction(HomeActionBus.ShowError(result.message))
                }

                is Resource.Loading -> setLoading(true)
                is Resource.Success -> {
                    setLoading(false)
                    sendAction(HomeActionBus.Accepted)
                }
            }
        }
    }
}
