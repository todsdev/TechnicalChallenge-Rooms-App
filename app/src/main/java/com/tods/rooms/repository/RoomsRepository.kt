package com.tods.rooms.repository

import com.tods.rooms.data.remote.ServiceApi
import com.tods.rooms.util.Constants
import javax.inject.Inject

class RoomsRepository @Inject constructor(
    private val api: ServiceApi
) {

    suspend fun recoverCurrencyInformation(to: String, amount: Float) = api.recoverCurrencyInformation(to, Constants.DEFAULT_CURRENCY, amount, Constants.API_KEY)
}