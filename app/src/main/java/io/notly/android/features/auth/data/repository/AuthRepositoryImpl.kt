package io.notly.android.features.auth.data.repository

import io.notly.android.core.NetworkResult
import io.notly.android.features.auth.data.data_source.AuthRemoteDataSource
import io.notly.android.features.auth.domain.repository.AuthRepository
import io.notly.android.features.auth.domain.model.UserRequest
import io.notly.android.features.auth.domain.model.UserResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val remoteDataSource: AuthRemoteDataSource,
    private val coroutineScope: CoroutineScope) : AuthRepository {

    override suspend fun loginUser(userRequest: UserRequest): NetworkResult<UserResponse> {
        return withContext(coroutineScope.coroutineContext) {
            remoteDataSource.loginUser(userRequest)
        }
    }

    override suspend fun registerUser(userRequest: UserRequest): NetworkResult<UserResponse> {
        return withContext(coroutineScope.coroutineContext) {
            remoteDataSource.registerUser(userRequest)
        }
    }
}