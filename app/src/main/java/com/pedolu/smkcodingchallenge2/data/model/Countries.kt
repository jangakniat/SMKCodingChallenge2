package com.pedolu.smkcodingchallenge2.data.model


import com.google.gson.annotations.SerializedName

data class Countries(
    @SerializedName("iso2")
    val iso2: String,
    @SerializedName("iso3")
    val iso3: String,
    @SerializedName("name")
    val name: String
)