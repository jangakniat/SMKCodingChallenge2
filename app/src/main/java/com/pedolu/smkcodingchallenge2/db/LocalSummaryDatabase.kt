package com.pedolu.smkcodingchallenge2.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pedolu.smkcodingchallenge2.dao.LocalSummaryDao
import com.pedolu.smkcodingchallenge2.data.model.room.LocalSummaryModel

@Database(entities = [LocalSummaryModel::class], version = 1, exportSchema = false)
abstract class LocalSummaryDatabase : RoomDatabase() {

    abstract fun LocalSummaryDao(): LocalSummaryDao

    companion object {
        @Volatile
        private var INSTANCE: LocalSummaryDatabase? = null

        fun getDatabase(context: Context): LocalSummaryDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocalSummaryDatabase::class.java,
                    "local_summary"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
