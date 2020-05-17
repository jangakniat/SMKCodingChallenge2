package com.pedolu.smkcodingchallenge2.data.service

import com.pedolu.smkcodingchallenge2.data.model.Countries
import com.pedolu.smkcodingchallenge2.data.model.CountrySummary
import com.pedolu.smkcodingchallenge2.data.model.GlobalStatusSummaryItem
import com.pedolu.smkcodingchallenge2.data.model.GlobalSummary
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CovidMathdroidService {
    @GET("api/")
    fun getGlobal(): Call<GlobalSummary>

    @GET("api/countries/{country}")
    fun getCountry(@Path("country") country: String?): Call<CountrySummary>

    @GET("api/countries")
    fun getCountries(): Call<Countries>

    @GET("api/confirmed")
    fun getGlobalConfirmed(): Call<List<GlobalStatusSummaryItem>>

    @GET("api/recovered")
    fun getGlobalRecovered(): Call<List<GlobalStatusSummaryItem>>

    @GET("api/deaths")
    fun getGlobalDeaths(): Call<List<GlobalStatusSummaryItem>>
}