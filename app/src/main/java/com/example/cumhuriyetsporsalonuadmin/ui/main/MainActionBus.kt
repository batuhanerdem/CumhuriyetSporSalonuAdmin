package com.example.cumhuriyetsporsalonuadmin.ui.main

import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseActionBus

sealed interface MainActionBus : BaseActionBus {
    data object Init : MainActionBus
}