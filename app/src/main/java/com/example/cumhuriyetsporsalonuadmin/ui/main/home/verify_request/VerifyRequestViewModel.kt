package com.example.cumhuriyetsporsalonuadmin.ui.main.home.verify_request

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.domain.model.User
import com.example.cumhuriyetsporsalonuadmin.domain.use_case.AnswerVerifyRequestUseCase
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseViewModel
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerifyRequestViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val answerVerifyRequestUseCase: AnswerVerifyRequestUseCase,
) : BaseViewModel<VerifyRequestActionBus>() {
    var unverifiedList = listOf<User>()
    init {
        Log.d(TAG, "testingen: ")
    }
    fun getUnverifiedUsers() {
        firebaseRepository.getUnverifiedStudents().onEach { result ->
            when (result) {
                is Resource.Error -> {
                    setLoading(false)
                    sendAction(VerifyRequestActionBus.ShowError(result.message))
                }

                is Resource.Loading -> setLoading(true)
                is Resource.Success -> {
                    setLoading(false)
                    Log.d(TAG, "getUnverifiedUsers: $result, ${result.data}")
                    result.data?.let { list ->
                        unverifiedList = list
                        sendAction(VerifyRequestActionBus.ApplicationsLoaded)
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun answerRequest(user: User, isAccepted: Boolean) {
        viewModelScope.launch {
            answerVerifyRequestUseCase.execute(user.uid, isAccepted).collect {}
        }
    }
}
