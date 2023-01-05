package io.notly.android.features.auth.domain.model

data class UserRequest(
    val username: String?,
    val email: String,
    val password: String,
)