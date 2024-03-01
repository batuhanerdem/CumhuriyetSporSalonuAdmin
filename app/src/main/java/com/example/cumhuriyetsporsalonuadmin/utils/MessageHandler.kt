package com.example.cumhuriyetsporsalonuadmin.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import com.irozon.sneaker.Sneaker
import android.widget.Toast
import com.example.cumhuriyetsporsalonuadmin.R
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class MessageHandler @Inject constructor(
    @ActivityContext private val context: Context,
    private val activity: Activity
) {

    private val sneaker: Sneaker by lazy {
        Sneaker.with(activity)
    }

    // Api messages default
    private val successfulMessage = context.getString(R.string.default_success_message)
    private val errorMessage = context.getString(R.string.default_error_message)

    fun sneakerApiMessage(stateRequest: StateRequest, message: String? = null) {
        try {
            when (stateRequest) {
                StateRequest.Success -> sneaker.setMessage(message ?: successfulMessage).sneakSuccess()
                StateRequest.Error -> sneaker.setMessage(message ?: errorMessage).sneakError()
            }
        } catch (e: Exception) {
            toastApiMessage(stateRequest, message)
        }
    }

    fun toastApiMessage(stateRequest: StateRequest, message: String? = null) {
        try {
            when (stateRequest) {
                StateRequest.Success -> toastMessage(message ?: successfulMessage)
                StateRequest.Error -> toastMessage(message ?: errorMessage)
            }
        } catch (e: Exception) {
            Log.e("SnackBarHelper", "Show Api Response Toast Message")
        }
    }

    private fun toastMessage(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    enum class StateRequest {
        Success, Error
    }
}