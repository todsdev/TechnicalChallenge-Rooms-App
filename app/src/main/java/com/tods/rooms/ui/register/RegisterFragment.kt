package com.tods.rooms.ui.register

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.tods.rooms.databinding.FragmentRegisterBinding
import com.tods.rooms.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment: BaseFragment<FragmentRegisterBinding, RegisterViewModel>() {
    override val viewModel: RegisterViewModel by viewModels()

    override fun recoverViewBinding(inflater: LayoutInflater, container: ViewGroup?):
            FragmentRegisterBinding = FragmentRegisterBinding.inflate(inflater, container, false)

}