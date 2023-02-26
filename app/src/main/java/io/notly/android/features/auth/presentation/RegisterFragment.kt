package io.notly.android.features.auth.presentation

import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.notly.android.R
import io.notly.android.core.NetworkResult
import io.notly.android.databinding.FragmentRegisterBinding
import io.notly.android.features.auth.domain.model.UserRequest
import io.notly.android.utils.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val authViewModel: AuthViewModel by viewModels()
    private var passwordHidden = true

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)

        binding.goToSignin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        if(tokenManager.geToken() != null){
            findNavController().navigate(R.id.action_registerFragment_to_notesListingFragment)
        }

        binding.signUpBtn.setOnClickListener {

            val (username, email, password) = getUserRequest()
            val (valid, reason) = validateCredentials(username!!, email, password)

            it.hideKeyboard()

            if(valid){
                authViewModel.registerUser(UserRequest(username, email, password))
            }
            else{
                it.snack(reason)
            }
        }

        binding.passwordShow.setOnClickListener {

            if(passwordHidden){
                passwordHidden = false
                binding.passwordShow.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_eye_off))
                binding.passwordEt.transformationMethod = null
                binding.passwordEt.setSelection(binding.passwordEt.text.length)
            }
            else{
                passwordHidden = true
                binding.passwordShow.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_eye_open))
                binding.passwordEt.transformationMethod = PasswordTransformationMethod()
                binding.passwordEt.setSelection(binding.passwordEt.text.length)
            }

        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeApiResult()
    }

    private fun getUserRequest(): UserRequest {
        val username = binding.usernameEt.text.toString().trim()
        val email = binding.emailEt.text.toString().trim()
        val password = binding.passwordEt.text.toString().trim()
        return UserRequest(username, email, password)
    }

    private fun observeApiResult(){
        this.lifecycleScope.launch{
            authViewModel.apply {
                uiState.collect{
                    if(!it.isLoading){
                        binding.progress.hide()
                        if(!it.user?.token.isNullOrEmpty()){
                            /**If loading has ended and there is no error than save token
                            to local storage and navigate to next screen**/
                            it.user?.token?.let { token -> tokenManager.saveToken(token) }
                            findNavController().navigate(R.id.action_registerFragment_to_notesListingFragment)
                        }
                        else{
                            if(!it.errorMessage.isNullOrEmpty()) binding.signUpBtn.snack(it.errorMessage)
                        }
                    }
                    else{
                        binding.progress.show()
                    }
                }
            }
        }
    }

    private fun validateCredentials(username: String, email: String, password: String): Pair<Boolean, String>{
        var pair = Pair(true, "")
        if(username.isEmpty() || email.isEmpty() || password.isEmpty()){
            pair = Pair(false, "Please fill all fields to continue")
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            pair = Pair(false, "Please enter a valid email address")
        }
        else if(password.length < 6){
            pair = Pair(false, "Password length can't be less than 6")
        }

        return pair
    }

}