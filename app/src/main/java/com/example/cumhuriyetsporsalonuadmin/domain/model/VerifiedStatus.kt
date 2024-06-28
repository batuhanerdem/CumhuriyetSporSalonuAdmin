package com.example.cumhuriyetsporsalonuadmin.domain.model

enum class VerifiedStatus(val asString: String) {
    VERIFIED("Verified"), NOTANSWERED("Not Answered"), DENIED("Denied");
}

fun String.toVerifiedStatus(): VerifiedStatus? {
    return when (this) {
        "Verified" -> VerifiedStatus.VERIFIED
        "Not Answered" -> VerifiedStatus.NOTANSWERED
        "Denied" -> VerifiedStatus.DENIED
        else -> null
    }
}

fun Boolean.toVerifiedStatus(): VerifiedStatus {
    return when (this) {
        true -> VerifiedStatus.VERIFIED
        false -> VerifiedStatus.DENIED
    }

}