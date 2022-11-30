package com.tods.rooms.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tods.rooms.R
import com.tods.rooms.databinding.FragmentLoginBinding
import com.tods.rooms.model.User
import com.tods.rooms.ui.base.BaseFragment
import com.tods.rooms.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment: BaseFragment<FragmentLoginBinding, LoginViewModel>() {
    override val viewModel: LoginViewModel by viewModels()
    private var auth: FirebaseAuth = Firebase.auth

    override fun recoverViewBinding(inflater: LayoutInflater, container: ViewGroup?):
            FragmentLoginBinding = FragmentLoginBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configInitialSettings()
        configButtonLoginClickListener()
    }

    private fun configInitialSettings() {
        (activity as AppCompatActivity).supportActionBar!!.title = getString(R.string.login)
    }

    private fun configButtonLoginClickListener() {
        binding.buttonLogin.setOnClickListener {
            configValidation()
        }
    }

    private fun configValidation() {
        val email = binding.etLoginEmail.text.toString()
        val password = binding.etLoginPassword.text.toString()
        if(email.isNotEmpty()) {
            if(password.isNotEmpty()) {
                val user = User()
                user.email = email
                user.password = password
                configLogin(user)
            } else {
                toast(getString(R.string.empty_password))
            }
        } else {
            toast(getString(R.string.empty_email))
        }
    }

    private fun configLogin(user: User) {
        auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    toast(getString(R.string.logged_in_successfully))
                    val action = LoginFragmentDirections.actionLoginFragmentToDormsFragment()
                    findNavController().navigate(action)
                } else {
                    try {
                        throw task.exception!!
                    } catch(e: FirebaseAuthInvalidUserException) {
                        toast(getString(R.string.user_exception))
                    } catch(e: FirebaseAuthInvalidCredentialsException) {
                        toast(getString(R.string.account_exception))
                    } catch(e: Exception) {
                        toast(getString(R.string.failed_login))
                    }
                }
            }
    }
}