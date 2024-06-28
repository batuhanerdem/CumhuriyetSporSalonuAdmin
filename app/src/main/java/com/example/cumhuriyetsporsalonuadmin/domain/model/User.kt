package com.example.cumhuriyetsporsalonuadmin.domain.model

import com.example.cumhuriyetsporsalonuadmin.domain.model.firebase_collection.UserField

data class User(
    val uid: String,
    val email: String,
    var name: String? = null,
    var surname: String? = null,
    var age: String? = null,
    var height: String? = null,
    var weight: String? = null,
    var bmi: String? = null,
    var isVerified: VerifiedStatus = VerifiedStatus.NOTANSWERED,
    var lessonUids: List<String> = emptyList()
) {
    fun toHashMap(): HashMap<String, Any?> {
        return hashMapOf(
            UserField.UID.key to this.uid,
            UserField.NAME.key to this.name,
            UserField.SURNAME.key to this.surname,
            UserField.EMAIL.key to this.email,
            UserField.AGE.key to this.age,
            UserField.HEIGHT.key to this.height,
            UserField.WEIGHT.key to this.weight,
            UserField.LESSON_UIDS.key to this.lessonUids,
            UserField.IS_VERIFIED.key to this.isVerified.asString
        )
    }
}

typealias Student = User