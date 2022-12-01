package com.tods.rooms.data.model.api_model

import com.google.gson.annotations.SerializedName

data class QueryModel(
    @SerializedName("to")
    val to: String,
    @SerializedName("amount")
    val amount: Float
)
