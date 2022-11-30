package com.tods.rooms.ui.my_reservations

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.tods.rooms.databinding.FragmentMyReservationsBinding
import com.tods.rooms.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyReservationsFragment: BaseFragment<FragmentMyReservationsBinding, MyReservationsViewModel>() {
    override val viewModel: MyReservationsViewModel by viewModels()

    override fun recoverViewBinding(inflater: LayoutInflater, container: ViewGroup?):
            FragmentMyReservationsBinding = FragmentMyReservationsBinding.inflate(inflater, container, false)


}