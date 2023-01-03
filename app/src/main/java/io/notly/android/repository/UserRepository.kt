package io.notly.android.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.notly.android.features.auth.data.api.UserAPI
import io.notly.android.models.UserRequest
import io.notly.android.models.UserResponse
import io.notly.android.utils.Constants.STANDARD_ERROR
import io.notly.android.utils.Constants.TAG
import io.notly.android.core.NetworkResult.Success
import io.notly.android.core.NetworkResult.Error
import io.notly.android.core.NetworkResult
import io.notly.android.core.NetworkResult.Loading
import io.notly.android.utils.getErrorMessage
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class UserRepository @Inject constructor(private var userAPI: UserAPI) {

    private val _userResponseLiveData = MutableLiveData<NetworkResult<UserResponse>>()
    val userResponseLiveData: LiveData<NetworkResult<UserResponse>>
    get() = _userResponseLiveData

    suspend fun registerUser(userRequest: UserRequest){
        _userResponseLiveData.postValue(Loading())
        try {
            val response = userAPI.signup(userRequest)
            handleResponse(response)
        } catch (exception: IOException){
            val error = exception.localizedMessage ?: STANDARD_ERROR
            Log.d(TAG, error)
            _userResponseLiveData.postValue(Error(error))
        }
    }

    suspend fun loginUser(userRequest: UserRequest) {
        _userResponseLiveData.postValue(Loading())
        try {
            val response = userAPI.signin(userRequest)
            handleResponse(response)
        } catch (exception: IOException){
            val error = exception.localizedMessage ?: STANDARD_ERROR
            Log.d(TAG, error)
            _userResponseLiveData.postValue(Error(error))
        }
    }

    private fun handleResponse(response: Response<UserResponse>){
        if(response.isSuccessful){
            _userResponseLiveData.postValue(Success(response.body()!!))
        }
        else if(response.errorBody() != null){
            _userResponseLiveData.postValue(Error(response.errorBody()!!.getErrorMessage()))
        }
        else{
            _userResponseLiveData.postValue(Error("Oops... Something went wrong"))
        }
    }
}