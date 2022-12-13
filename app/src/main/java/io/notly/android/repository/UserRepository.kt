package io.notly.android.repository

import android.util.Log
import io.notly.android.api.UserAPI
import io.notly.android.models.UserRequest
import io.notly.android.utils.Constants.TAG
import javax.inject.Inject

class UserRepository @Inject constructor(private var userAPI: UserAPI) {

    suspend fun registerUser(userRequest: UserRequest){
        val response = userAPI.signup(userRequest)
        Log.d(TAG, response.toString())
    }

    suspend fun  loginUser(userRequest: UserRequest) {
        val response = userAPI.signin(userRequest)
        Log.d(TAG, response.toString())
    }
}