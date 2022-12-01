package com.tods.rooms.data.model.api_model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ResponseModel(
    @SerializedName("query")
    val query: QueryModel,
    @SerializedName("info")
    val info: InfoModel,
    @SerializedName("result")
    val result: Float
): Serializable
