package io.notly.android.features.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.notly.android.features.auth.domain.model.UserRequest
import io.notly.android.core.NetworkResult
import io.notly.android.features.auth.domain.use_case.AuthUseCases
import io.notly.android.features.auth.presentation.ui_state.AuthUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authUseCases: AuthUseCases): ViewModel() {

    private var _uiState = MutableStateFlow(AuthUiState())
    val uiState get() = _uiState

    private var loginUser: Job? = null
    private var registerUser: Job? = null

    fun registerUser(userRequest: UserRequest){
        registerUser?.cancel()

        registerUser = viewModelScope.launch {
            val result = authUseCases.registerUser(userRequest)
            _uiState.update { it.copy(isLoading = true) }

            when(result){
                is NetworkResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            user = result.data,
                            errorMessage = ""
                        )
                    }
                }
                is NetworkResult.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
                is NetworkResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message ?: "Something went wrong"
                        )
                    }
                }
            }
        }
    }

    fun loginUser(userRequest: UserRequest){

        loginUser?.cancel()

        loginUser = viewModelScope.launch {
            val result = authUseCases.loginUser(userRequest)
            _uiState.update { it.copy(isLoading = true) }

            when(result){
                is NetworkResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            user = result.data,
                            errorMessage = ""
                        )
                    }
                }
                is NetworkResult.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
                is NetworkResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message ?: "Something went wrong"
                        )
                    }
                }
            }
        }

    }



}