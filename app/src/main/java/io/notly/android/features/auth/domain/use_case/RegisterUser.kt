package io.notly.android.features.auth.domain.use_case

import io.notly.android.core.NetworkResult
import io.notly.android.features.auth.domain.model.UserRequest
import io.notly.android.features.auth.domain.model.UserResponse
import io.notly.android.features.auth.domain.repository.AuthRepository

class RegisterUser (private val authRepository: AuthRepository) {

    suspend operator fun invoke(userRequest: UserRequest) : NetworkResult<UserResponse> {
        return authRepository.registerUser(userRequest)
    }
}