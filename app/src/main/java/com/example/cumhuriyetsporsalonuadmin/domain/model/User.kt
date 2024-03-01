package com.example.cumhuriyetsporsalonuadmin.domain.model

data class User(
    val uid: String,
    val email: String,
    var name: String? = null,
    var surname: String? = null,
    var age: String? = null,
    var height: String? = null,
    var weight: String? = null,
    var isVerified: Boolean = false
)