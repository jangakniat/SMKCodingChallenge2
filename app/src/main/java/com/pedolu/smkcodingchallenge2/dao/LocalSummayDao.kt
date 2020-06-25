package com.pedolu.smkcodingchallenge2.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pedolu.smkcodingchallenge2.data.model.room.LocalSummaryModel

@Dao
interface LocalSummaryDao {
    @Query("SELECT * from local_summary WHERE country_name=:country")
    fun getLocalSummary(country: String): LiveData<LocalSummaryModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(localSummary: LocalSummaryModel)

}