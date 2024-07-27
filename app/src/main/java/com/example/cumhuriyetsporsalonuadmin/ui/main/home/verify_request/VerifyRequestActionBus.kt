package com.example.cumhuriyetsporsalonuadmin.ui.main.home.verify_request

import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseActionBus
import com.example.cumhuriyetsporsalonuadmin.utils.Stringfy

sealed interface VerifyRequestActionBus : BaseActionBus {
    data object Init : VerifyRequestActionBus

    data object ApplicationsLoaded : VerifyRequestActionBus

    data class ShowError(val error: Stringfy? = null) : VerifyRequestActionBus

    data object Accepted : VerifyRequestActionBus

}