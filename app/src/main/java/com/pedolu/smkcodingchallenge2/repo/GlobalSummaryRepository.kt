package com.pedolu.smkcodingchallenge2.repo

import androidx.lifecycle.LiveData
import com.pedolu.smkcodingchallenge2.dao.GlobalSummaryDao
import com.pedolu.smkcodingchallenge2.data.model.room.GlobalSummaryModel

class GlobalSummaryRepository(private val globalSummaryDao: GlobalSummaryDao) {
    val globalSummary: LiveData<GlobalSummaryModel> = globalSummaryDao.getGlobalSummary()

    suspend fun insert(globalSummary: GlobalSummaryModel) {
        globalSummaryDao.insert(globalSummary)
    }

    suspend fun update(globalSummary: GlobalSummaryModel) {
        globalSummaryDao.update(globalSummary)
    }

    suspend fun delete(globalSummary: GlobalSummaryModel) {
        globalSummaryDao.delete(globalSummary)
    }
}