package com.pedolu.smkcodingchallenge2.data.model.status


import com.google.gson.annotations.SerializedName

data class Deaths(
    @SerializedName("detail")
    val detail: String,
    @SerializedName("value")
    val value: Int
)