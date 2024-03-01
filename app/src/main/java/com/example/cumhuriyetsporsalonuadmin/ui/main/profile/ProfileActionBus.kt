package com.example.cumhuriyetsporsalonuadmin.ui.main.profile

import com.example.cumhuriyetsporsalonuadmin.ui.base.BaseActionBus
import com.example.cumhuriyetsporsalonuadmin.utils.Stringfy

sealed interface ProfileActionBus : BaseActionBus {
    data object Init : ProfileActionBus

    data object UpdatedSuccessFully : ProfileActionBus

    data class ShowError(val error: Stringfy?) : ProfileActionBus

}