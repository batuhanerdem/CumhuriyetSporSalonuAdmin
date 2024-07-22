package com.example.cumhuriyetsporsalonuadmin.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cumhuriyetsporsalonuadmin.utils.Stringfy
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

open class BaseViewModel<ActionBus : BaseActionBus> : ViewModel() {

    val TAG = "tag"

    val actionBus: MutableSharedFlow<ActionBus> = MutableSharedFlow()
    val messageBus: MutableSharedFlow<Stringfy> = MutableSharedFlow()

    fun sendAction(action: ActionBus) {
        viewModelScope.launch {
            actionBus.emit(action)
        }
    }

    fun sendMessage(message: Stringfy) {
        viewModelScope.launch {
            messageBus.emit(message)
        }
    }

    fun init() {}

    // region Loading states
    private val mLoadingState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean>
        get() = mLoadingState

    private var loadingCancelJob = loadingCancel()

    private fun loadingCancel() = viewModelScope.launch {
//        delay(150)
        mLoadingState.value = false
    }

    fun setLoading(loading: Boolean) {
        if (loadingCancelJob.isCompleted || loadingCancelJob.isCancelled) {
            loadingCancelJob = loadingCancel()
        }

        if (loading) {
            if (loadingCancelJob.isActive) {
                loadingCancelJob.cancel()
            }
            mLoadingState.value = true
        } else {
            loadingCancelJob.start()
        }
    }
    // endregion
}