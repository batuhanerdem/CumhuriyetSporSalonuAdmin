package com.example.cumhuriyetsporsalonuadmin.ui.auth.login

import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseActionBus
import com.example.cumhuriyetsporsalonuadmin.utils.Stringfy

sealed interface LoginActionBus : BaseActionBus {

    data object Init : LoginActionBus

    data object LoggedIn : LoginActionBus

    data class ShowError(val error: Stringfy?) : LoginActionBus

}