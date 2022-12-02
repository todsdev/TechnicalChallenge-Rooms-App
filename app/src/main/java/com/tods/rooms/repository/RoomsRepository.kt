package com.tods.rooms.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.tods.rooms.data.model.Reservation
import com.tods.rooms.data.model.api_model.ResponseModel
import com.tods.rooms.data.remote.ServiceApi
import com.tods.rooms.state.ResourceState
import com.tods.rooms.util.Constants
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class RoomsRepository @Inject constructor(
    private val api: ServiceApi,
) {

    suspend fun recoverCurrencyInformation(to: String, amount: Float) =
        api.recoverCurrencyInformation(to, Constants.DEFAULT_CURRENCY, amount, Constants.API_KEY)
}