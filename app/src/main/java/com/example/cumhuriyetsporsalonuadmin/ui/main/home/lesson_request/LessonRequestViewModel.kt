package com.example.cumhuriyetsporsalonuadmin.ui.main.home.lesson_request

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.cumhuriyetsporsalonuadmin.domain.model.LessonRequest
import com.example.cumhuriyetsporsalonuadmin.domain.use_case.AnswerLessonRequestUseCase
import com.example.cumhuriyetsporsalonuadmin.domain.use_case.GetLessonRequestUseCase
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseViewModel
import com.example.cumhuriyetsporsalonuadmin.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LessonRequestViewModel @Inject constructor(
    private val answerLessonRequestUseCase: AnswerLessonRequestUseCase,
    private val getLessonRequestUseCase: GetLessonRequestUseCase
) : BaseViewModel<LessonRequestActionBus>() {
    private var _requestList = listOf<LessonRequest>()
    val requestList: List<LessonRequest> get() = _requestList


    fun answerRequest(lessonRequest: LessonRequest, isAccepted: Boolean) {
        viewModelScope.launch {
            answerLessonRequestUseCase.execute(lessonRequest, isAccepted).collect { result ->
                when (result) {
                    is Resource.Error -> {
                        setLoading(false)
                        sendAction(LessonRequestActionBus.ShowError(result.message))
                    }

                    is Resource.Loading -> setLoading(true)
                    is Resource.Success -> {
                        setLoading(false)
                        result.data?.let {
                            sendAction(LessonRequestActionBus.Answered)
                        }
                    }
                }
            }
        }
    }

    fun getRequest() {
        viewModelScope.launch {
            getLessonRequestUseCase.execute().collect { result ->
                when (result) {
                    is Resource.Error -> {
                        setLoading(false)
                    }

                    is Resource.Loading -> setLoading(true)
                    is Resource.Success -> {
                        setLoading(false)
                        Log.d(TAG, "getRequest: $result \n ${result.data}")
                        result.data?.let {
                            _requestList = it
                            sendAction(LessonRequestActionBus.ApplicationsLoaded)
                        }
                    }
                }

            }
        }
    }
}
