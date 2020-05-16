package com.pedolu.smkcodingchallenge2.data.dao

import com.pedolu.smkcodingchallenge2.data.model.ProvinsiItem

import retrofit2.Call
import retrofit2.http.GET

interface CovidKawalCoronaService {
    @GET("indonesia/provinsi")
    fun getProvinsi(): Call<List<ProvinsiItem>>
}