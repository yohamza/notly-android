package io.notly.android

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.notly.android.databinding.FragmentLoginBinding
import io.notly.android.models.UserRequest
import io.notly.android.utils.*
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val authViewModel: AuthViewModel by viewModels()

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.goToSignup.setOnClickListener {
                findNavController().popBackStack()
        }

        binding.signInBtn.setOnClickListener {

            val userRequest = getUserRequest()

            val (valid, reason) = validateCredentials(userRequest.email, userRequest.password)

            it.hideKeyboard()

            if(valid){
                authViewModel.loginUser(UserRequest("", userRequest.email, userRequest.password))
            }
            else{
                it.snack(reason)
            }
        }

        return binding.root
    }

    private fun getUserRequest(): UserRequest{
        val email = binding.emailEt.text.toString().trim()
        val password = binding.passwordEt.text.toString().trim()
        return UserRequest(null, email, password)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner) {

            binding.progress.hide()

            when(it){
                is NetworkResult.Success -> {
                    tokenManager.saveToken(it.data!!.token)
                    findNavController().navigate(R.id.action_loginFragment_to_notesListingFragment)
                }
                is NetworkResult.Error -> {
                    binding.signInBtn.snack(it.message!!)
                }
                is NetworkResult.Loading -> {
                    binding.progress.show()
                }
            }
        }
    }

    private fun validateCredentials(email: String, password: String): Pair<Boolean, String>{
        var pair = Pair(true, "")
        if(email.isEmpty() || password.isEmpty()){
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