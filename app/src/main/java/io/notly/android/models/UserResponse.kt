package io.notly.android.models

data class UserResponse(
    val user: User,
    val message: String,
    val token: String
)