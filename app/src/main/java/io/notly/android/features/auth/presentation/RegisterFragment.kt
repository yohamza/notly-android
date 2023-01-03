package io.notly.android.features.auth.presentation

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.notly.android.R
import io.notly.android.core.NetworkResult
import io.notly.android.databinding.FragmentRegisterBinding
import io.notly.android.models.UserRequest
import io.notly.android.utils.*
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val authViewModel: AuthViewModel by viewModels()

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

        //TODO: Implement this code on a splash screen
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

        return binding.root
    }

    private fun getUserRequest(): UserRequest{
        val username = binding.usernameEt.text.toString().trim()
        val email = binding.emailEt.text.toString().trim()
        val password = binding.passwordEt.text.toString().trim()
        return UserRequest(username, email, password)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner) {

            binding.progress.hide()

            when(it){
                is NetworkResult.Success -> {
                    tokenManager.saveToken(it.data!!.token)
                    findNavController().navigate(R.id.action_registerFragment_to_notesListingFragment)
                }
                is NetworkResult.Error -> {
                    binding.signUpBtn.snack(it.message!!)
                }
                is NetworkResult.Loading -> {
                    binding.progress.show()
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