package com.example.cumhuriyetsporsalonuadmin.domain.model.firebase_exception

import com.example.cumhuriyetsporsalonuadmin.R
import com.example.cumhuriyetsporsalonuadmin.utils.Stringfy
import com.example.cumhuriyetsporsalonuadmin.utils.Stringfy.Companion.stringfy


enum class RegisterError(val errorMessage: Stringfy) {
    UserCollisionException(R.string.register_error_user_collision.stringfy()),
    InvalidCredentialsException(R.string.register_error_invalid_credantial.stringfy()),
    WeakPasswordException(R.string.register_error_weak_password.stringfy()),
    NetworkException(R.string.firebase_error_network.stringfy()),
    TooManyRequestsException(R.string.firebase_error_too_many_request.stringfy()),
}