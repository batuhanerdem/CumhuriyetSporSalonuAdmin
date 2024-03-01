package com.example.cumhuriyetsporsalonuadmin.ui.main.home

import com.example.cumhuriyetsporsalonuadmin.domain.model.User
import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseActionBus

sealed interface HomeActionBus : BaseActionBus {
    data object Init : HomeActionBus

    data class Success(val list: List<User>) : HomeActionBus

    data object Accepted : HomeActionBus

}