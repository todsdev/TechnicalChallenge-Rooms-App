package com.tods.rooms.ui.my_reservation_details

import androidx.lifecycle.ViewModel
import com.tods.rooms.repository.RoomsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyReservationDetailsViewModel @Inject constructor(
    val repository: RoomsRepository
): ViewModel() {
}