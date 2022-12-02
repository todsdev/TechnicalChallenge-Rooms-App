package com.tods.rooms.ui.my_reservation_details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.tods.rooms.databinding.FragmentMyReservationDetailsBinding
import com.tods.rooms.ui.base.BaseFragment


class MyReservationDetailsFragment: BaseFragment<FragmentMyReservationDetailsBinding, MyReservationDetailsViewModel>() {
    override val viewModel: MyReservationDetailsViewModel by viewModels()

    override fun recoverViewBinding(inflater: LayoutInflater, container: ViewGroup?):
            FragmentMyReservationDetailsBinding = FragmentMyReservationDetailsBinding.inflate(inflater, container, false)
}