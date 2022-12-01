package com.tods.rooms.data.remote

import com.tods.rooms.data.model.api_model.ResponseModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ServiceApi {

    @GET("convert")
    suspend fun recoverCurrencyInformation(
        @Query("to") chosenCurrency: String,
        @Query("from") constantCurrency: String,
        @Query("amount") amount: Float,
        @Query("apikey") apiKey: String
    ): Response<ResponseModel>

}