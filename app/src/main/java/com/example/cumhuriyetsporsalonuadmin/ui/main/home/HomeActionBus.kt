package com.example.cumhuriyetsporsalonuadmin.ui.main.home

import com.example.cumhuriyetsporsalonuadmin.domain.model.User
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseActionBus

sealed interface HomeActionBus : BaseActionBus {
    data object Init : HomeActionBus

    data object Success : HomeActionBus

    data object Accepted : HomeActionBus

}