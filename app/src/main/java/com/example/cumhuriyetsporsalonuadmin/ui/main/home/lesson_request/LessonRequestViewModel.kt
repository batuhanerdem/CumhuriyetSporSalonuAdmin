package com.example.cumhuriyetsporsalonuadmin.ui.main.home.lesson_request

import androidx.lifecycle.viewModelScope
import com.example.cumhuriyetsporsalonuadmin.domain.model.LessonRequest
import com.example.cumhuriyetsporsalonuadmin.domain.use_case.AnswerLessonRequestUseCase
import com.example.cumhuriyetsporsalonuadmin.domain.use_case.GetLessonRequestUseCase
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseViewModel
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LessonRequestViewModel @Inject constructor(
    private val answerLessonRequestUseCase: AnswerLessonRequestUseCase,
    private val getLessonRequestUseCase: GetLessonRequestUseCase
) : BaseViewModel<LessonRequestActionBus>() {
    private var _requestList = mutableListOf<LessonRequest>()
    val requestList: List<LessonRequest> get() = _requestList


    fun answerRequest(lessonRequest: LessonRequest, isAccepted: Boolean) {
        setLoading(true)
        answerLessonRequestUseCase.execute(lessonRequest, isAccepted).onEach { result ->
            setLoading(false)
            when (result) {
                is Resource.Error -> {
                    sendAction(LessonRequestActionBus.ShowError(result.message))
                }

                is Resource.Success -> {
                    sendAction(LessonRequestActionBus.Answered)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getRequest() {
        setLoading(true)
        viewModelScope.launch {
            getLessonRequestUseCase.execute().collect { result ->
                setLoading(false)
                when (result) {
                    is Resource.Error -> {
                        sendAction(LessonRequestActionBus.ShowError(result.message))
                    }

                    is Resource.Success -> {
                        result.data?.let {
                            _requestList = it.toMutableList()
                            sendAction(LessonRequestActionBus.ApplicationsLoaded)
                        }
                    }
                }
            }
        }
    }
}
