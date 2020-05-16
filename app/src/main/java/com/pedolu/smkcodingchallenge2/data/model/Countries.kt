package com.pedolu.smkcodingchallenge2.data.model


import com.google.gson.annotations.SerializedName

data class Countries(
    @SerializedName("countries")
    val countries: List<Country>
)