package io.notly.android.features.auth.domain.repository

import io.notly.android.core.NetworkResult
import io.notly.android.features.auth.domain.model.UserRequest
import io.notly.android.features.auth.domain.model.UserResponse

interface AuthRepository {
    suspend fun loginUser(userRequest: UserRequest) : NetworkResult<UserResponse>
    suspend fun registerUser(userRequest: UserRequest) : NetworkResult<UserResponse>
}