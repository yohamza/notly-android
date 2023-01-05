package io.notly.android.features.auth.data.data_source

import io.notly.android.core.NetworkResult
import io.notly.android.core.NetworkResult.*
import io.notly.android.features.auth.data.api.UserAPI
import io.notly.android.features.auth.domain.model.UserRequest
import io.notly.android.features.auth.domain.model.UserResponse
import io.notly.android.utils.getErrorMessage
import io.notly.android.utils.nonNullMessage
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class AuthRemoteDataSource @Inject constructor(private val userAPI: UserAPI) {

    suspend fun registerUser(userRequest: UserRequest): NetworkResult<UserResponse>{
        return try {
            val response = userAPI.signup(userRequest)
            handleResponse(response)
        } catch (exception: IOException){
            Error(exception.nonNullMessage())
        }
    }

    suspend fun loginUser(userRequest: UserRequest): NetworkResult<UserResponse> {
        return try {
            val response = userAPI.signin(userRequest)
            handleResponse(response)
        } catch (exception: IOException){
            Error(exception.nonNullMessage())
        }
    }

    private fun handleResponse(response: Response<UserResponse>): NetworkResult<UserResponse>{
        return if(response.isSuccessful){
            Success(response.body()!!)
        }
        else if(response.errorBody() != null){
            Error(response.errorBody()!!.getErrorMessage())
        }
        else{
            Error("Oops... Something went wrong")
        }
    }

}