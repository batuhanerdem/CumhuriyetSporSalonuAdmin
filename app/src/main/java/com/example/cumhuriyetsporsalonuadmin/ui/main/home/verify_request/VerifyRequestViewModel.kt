package com.example.cumhuriyetsporsalonuadmin.ui.main.home.verify_request

import androidx.lifecycle.viewModelScope
import com.example.cumhuriyetsporsalonuadmin.data.repository.FirebaseRepository
import com.example.cumhuriyetsporsalonuadmin.domain.model.User
import com.example.cumhuriyetsporsalonuadmin.domain.use_case.AnswerVerifyRequestUseCase
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseViewModel
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class VerifyRequestViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val answerVerifyRequestUseCase: AnswerVerifyRequestUseCase,
) : BaseViewModel<VerifyRequestActionBus>() {
    var unverifiedList = listOf<User>()

    fun getUnverifiedUsers() {
        setLoading(true)
        firebaseRepository.getUnverifiedStudents().onEach { result ->
            setLoading(false)
            when (result) {
                is Resource.Error -> {
                    setLoading(false)
                    sendAction(VerifyRequestActionBus.ShowError(result.message))
                }

                is Resource.Success -> {
                    setLoading(false)
                    result.data?.let { list ->
                        unverifiedList = list
                        sendAction(VerifyRequestActionBus.ApplicationsLoaded)
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun answerRequest(user: User, isAccepted: Boolean) {
        answerVerifyRequestUseCase.execute(user.uid, isAccepted).onEach {}.launchIn(viewModelScope)
    }
}
