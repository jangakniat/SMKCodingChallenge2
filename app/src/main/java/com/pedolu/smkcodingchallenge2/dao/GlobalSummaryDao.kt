package com.pedolu.smkcodingchallenge2.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.pedolu.smkcodingchallenge2.data.model.room.GlobalSummaryModel

@Dao
interface GlobalSummaryDao {
    @Query("SELECT * from global_summary")
    fun getGlobalSummary(): LiveData<GlobalSummaryModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: GlobalSummaryModel)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(user: GlobalSummaryModel)

    @Delete()
    suspend fun delete(user: GlobalSummaryModel)
}