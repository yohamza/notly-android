package io.notly.android.features.auth.presentation.ui_state

import io.notly.android.features.auth.domain.model.UserResponse

data class AuthUiState(
    val isLoading: Boolean = true,
    val errorMessage: String = "",
    val user: UserResponse? = null,
)
