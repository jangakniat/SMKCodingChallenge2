package com.pedolu.smkcodingchallenge2.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pedolu.smkcodingchallenge2.dao.CountriesDao
import com.pedolu.smkcodingchallenge2.data.model.room.CountriesModel

@Database(entities = [CountriesModel::class], version = 1, exportSchema = false)
abstract class CountriesDatabase : RoomDatabase() {

    abstract fun CountriesDao(): CountriesDao

    companion object {
        @Volatile
        private var INSTANCE: CountriesDatabase? = null

        fun getDatabase(context: Context): CountriesDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CountriesDatabase::class.java,
                    "countries"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
