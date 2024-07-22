package com.example.cumhuriyetsporsalonuadmin.ui.main.home

import androidx.lifecycle.viewModelScope
import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.domain.model.User
import com.example.cumhuriyetsporsalonuadmin.domain.use_case.AnswerRequestUseCase
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseViewModel
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeVIewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val answerRequestUseCase: AnswerRequestUseCase
) : BaseViewModel<HomeActionBus>() {
    var unverifiedList = listOf<User>()

    fun getUnverifiedUsers() {
        firebaseRepository.getUnverifiedStudents().onEach { result ->
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
        }.launchIn(viewModelScope)
    }

    fun answerRequest(user: User, isAccepted: Boolean) {
        viewModelScope.launch {
            answerRequestUseCase.execute(user.uid, isAccepted).collect {}
        }
    }
}
