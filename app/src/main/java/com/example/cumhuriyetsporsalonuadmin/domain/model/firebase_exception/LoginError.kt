package com.example.cumhuriyetsporsalonuadmin.domain.model.firebase_exception

import com.example.cumhuriyetsporsalonuadmin.R
import com.example.cumhuriyetsporsalonuadmin.utils.Stringfy
import com.example.cumhuriyetsporsalonuadmin.utils.Stringfy.Companion.stringfy

enum class LoginError(val errorMessage: Stringfy) {
    InvalidCredentialsException(R.string.login_error_invalid_credentials.stringfy()),
    InvalidUserException(R.string.login_error_invalid_user.stringfy()),
    UserException(R.string.login_error_user.stringfy()),
    NetworkException(R.string.firebase_error_network.stringfy()),
    TooManyRequestsException(R.string.firebase_error_too_many_request.stringfy()),
    RecentLoginRequiredException(R.string.login_error_recent_login.stringfy()),
    EmptySpaces(R.string.login_error_empty_space.stringfy()),
}