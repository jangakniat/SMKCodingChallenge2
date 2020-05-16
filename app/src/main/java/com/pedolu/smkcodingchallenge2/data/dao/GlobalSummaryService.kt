package com.pedolu.smkcodingchallenge2.data.dao

import com.pedolu.smkcodingchallenge2.data.model.Countries
import com.pedolu.smkcodingchallenge2.data.model.CountrySummary
import com.pedolu.smkcodingchallenge2.data.model.GlobalSummary
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GlobalSummaryService {

        @GET("api/countries/{country}")
        fun getCountry(@Path("country") country: String?): Call<CountrySummary>

        @GET("api/countries")
        fun getCountries(): Call<Countries>

        @GET("api/")
        fun getGlobal(): Call<GlobalSummary>


}