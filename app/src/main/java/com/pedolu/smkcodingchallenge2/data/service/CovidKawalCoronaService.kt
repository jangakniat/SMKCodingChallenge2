package com.pedolu.smkcodingchallenge2.data.service

import com.pedolu.smkcodingchallenge2.data.model.indonesia.ProvinsiItem

import retrofit2.Call
import retrofit2.http.GET

interface CovidKawalCoronaService {
    @GET("indonesia/provinsi")
    fun getProvinsi(): Call<List<ProvinsiItem>>
}