package com.pedolu.smkcodingchallenge2.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pedolu.smkcodingchallenge2.dao.UserDao
import com.pedolu.smkcodingchallenge2.data.model.room.UserModel

@Database(entities = arrayOf(UserModel::class), version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {

    abstract fun UserDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getDatabase(context: Context): UserDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "users"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
