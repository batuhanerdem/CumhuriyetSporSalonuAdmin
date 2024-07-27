package com.example.cumhuriyetsporsalonuadmin.ui.main.home

import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseActionBus
import com.example.cumhuriyetsporsalonuadmin.utils.Stringfy

sealed interface HomeActionBus : BaseActionBus {
    data object Init : HomeActionBus


    data class ShowError(val error: Stringfy? = null) : HomeActionBus


}