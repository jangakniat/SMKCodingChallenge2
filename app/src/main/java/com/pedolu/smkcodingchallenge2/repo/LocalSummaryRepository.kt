package com.pedolu.smkcodingchallenge2.repo

import androidx.lifecycle.LiveData
import com.pedolu.smkcodingchallenge2.dao.LocalSummaryDao
import com.pedolu.smkcodingchallenge2.data.model.room.LocalSummaryModel

class LocalSummaryRepository(
    private val localSummaryDao: LocalSummaryDao,
    private val country: String
) {
    val localSummary: LiveData<LocalSummaryModel> = localSummaryDao.getLocalSummary(country)

    suspend fun insert(localSummary: LocalSummaryModel) {
        localSummaryDao.insert(localSummary)
    }

}