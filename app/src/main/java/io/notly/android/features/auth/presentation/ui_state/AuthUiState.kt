package io.notly.android.features.auth.presentation.ui_state

import io.notly.android.features.auth.domain.model.UserResponse

data class AuthUiState(
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val user: UserResponse? = null,
)
