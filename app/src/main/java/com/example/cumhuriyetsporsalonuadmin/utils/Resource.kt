package com.example.cumhuriyetsporsalonuadmin.utils

sealed class Resource<T>(
    val data: T? = null, val message: Stringfy? = null
) {
    class Success<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(message: Stringfy? = null, data: T? = null) : Resource<T>(data, message)

}