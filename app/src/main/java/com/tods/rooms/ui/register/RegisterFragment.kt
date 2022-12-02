package com.tods.rooms.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tods.rooms.R
import com.tods.rooms.databinding.FragmentRegisterBinding
import com.tods.rooms.data.model.firebase_model.User
import com.tods.rooms.ui.base.BaseFragment
import com.tods.rooms.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment: BaseFragment<FragmentRegisterBinding, RegisterViewModel>() {
    override val viewModel: RegisterViewModel by viewModels()
    private var auth: FirebaseAuth = Firebase.auth

    override fun recoverViewBinding(inflater: LayoutInflater, container: ViewGroup?):
            FragmentRegisterBinding = FragmentRegisterBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configInitialSettings()
        configButtonRegisterClickListener()
    }

    private fun configInitialSettings() {
        (activity as AppCompatActivity).supportActionBar!!.title = getString(R.string.register)
    }

    private fun configButtonRegisterClickListener() {
        binding.buttonRegister.setOnClickListener {
            configValidation()
        }
    }

    private fun configValidation() {
        val name = binding.etRegisterName.text.toString()
        val email = binding.etRegisterEmail.text.toString()
        val password = binding.etRegisterPassword.text.toString()
        val passwordConfirm = binding.etRegisterConfirmPassword.text.toString()
        if(name.isNotEmpty()) {
            if(email.isNotEmpty()) {
                if(password.isNotEmpty()) {
                    if(passwordConfirm.isNotEmpty()) {
                        if(password == passwordConfirm) {
                            val user = User()
                            user.name = name
                            user.email = email
                            user.password = password
                            configRegistration(user)
                        } else {
                            toast(getString(R.string.failed_password_match))
                        }
                    } else {
                        toast(getString(R.string.empty_password_confirm))
                    }
                } else {
                    toast(getString(R.string.empty_password))
                }
            } else {
                toast(getString(R.string.empty_email))
            }
        } else {
            toast(getString(R.string.empty_name))
        }
    }

    private fun configRegistration(user: User) {
        auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(user.email, user.password).addOnCompleteListener { task ->
            if(task.isSuccessful) {
                user.save()
                toast(getString(R.string.registered_successfully))
                val action = RegisterFragmentDirections.actionRegisterFragmentToDormsFragment()
                findNavController().navigate(action)
            } else {
                try {
                    throw task.exception!!
                } catch(e: FirebaseAuthWeakPasswordException) {
                    toast(getString(R.string.strong_password))
                } catch(e: FirebaseAuthInvalidCredentialsException) {
                    toast(getString(R.string.valid_email))
                } catch(e: FirebaseAuthUserCollisionException) {
                    toast(getString(R.string.chosen_email))
                } catch(e: Exception) {
                    toast(getString(R.string.failed_registration))
                }
            }
        }
    }
}