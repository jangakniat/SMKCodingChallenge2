package com.pedolu.smkcodingchallenge2.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pedolu.smkcodingchallenge2.dao.GlobalSummaryDao
import com.pedolu.smkcodingchallenge2.data.model.room.GlobalSummaryModel

@Database(entities = arrayOf(GlobalSummaryModel::class), version = 1, exportSchema = false)
abstract class GlobalSummaryDatabase : RoomDatabase() {

    abstract fun GlobalSummaryDao(): GlobalSummaryDao

    companion object {
        @Volatile
        private var INSTANCE: GlobalSummaryDatabase? = null

        fun getDatabase(context: Context): GlobalSummaryDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GlobalSummaryDatabase::class.java,
                    "global_summary"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
