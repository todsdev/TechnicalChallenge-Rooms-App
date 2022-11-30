package com.tods.rooms.ui.room

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.tods.rooms.databinding.FragmentRoomBinding
import com.tods.rooms.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoomFragment: BaseFragment<FragmentRoomBinding, RoomViewModel>() {
    override val viewModel: RoomViewModel by viewModels()

    override fun recoverViewBinding(inflater: LayoutInflater, container: ViewGroup?):
            FragmentRoomBinding = FragmentRoomBinding.inflate(inflater, container, false)


}