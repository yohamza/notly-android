package io.notly.android.features.auth.domain.model

data class UserResponse(
    val user: User,
    val message: String,
    val token: String
)