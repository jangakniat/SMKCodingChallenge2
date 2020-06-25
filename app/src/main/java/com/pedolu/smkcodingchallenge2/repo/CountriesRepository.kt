package com.pedolu.smkcodingchallenge2.repo

import androidx.lifecycle.LiveData
import com.pedolu.smkcodingchallenge2.dao.CountriesDao
import com.pedolu.smkcodingchallenge2.data.model.room.CountriesModel

class CountriesRepository(private val countriesDao: CountriesDao) {
    val countries: LiveData<List<CountriesModel>> = countriesDao.getCountries()

    suspend fun insertAll(countries: List<CountriesModel>) {
        countriesDao.insertAll(countries)
    }

    suspend fun deleteAll() {
        countriesDao.deleteAll()
    }


}