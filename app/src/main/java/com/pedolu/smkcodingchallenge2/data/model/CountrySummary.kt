package com.pedolu.smkcodingchallenge2.data.model


import com.google.gson.annotations.SerializedName

data class CountrySummary(
    @SerializedName("confirmed")
    val confirmed: Confirmed,
    @SerializedName("deaths")
    val deaths: Deaths,
    @SerializedName("lastUpdate")
    val lastUpdate: String,
    @SerializedName("recovered")
    val recovered: Recovered
)