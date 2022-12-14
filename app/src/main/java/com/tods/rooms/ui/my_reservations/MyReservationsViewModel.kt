package com.tods.rooms.ui.my_reservations

import androidx.lifecycle.ViewModel
import com.tods.rooms.data.model.firebase_model.Reservation
import com.tods.rooms.repository.RoomsRepository
import com.tods.rooms.state.ResourceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MyReservationsViewModel @Inject constructor(
    val repository: RoomsRepository
): ViewModel() {
    private var _reservations = MutableStateFlow<ResourceState<List<Reservation>>>(ResourceState.Empty())
    var reservations: StateFlow<ResourceState<List<Reservation>>> = _reservations
}