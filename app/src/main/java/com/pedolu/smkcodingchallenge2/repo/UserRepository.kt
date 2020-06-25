package com.pedolu.smkcodingchallenge2.repo

import androidx.lifecycle.LiveData
import com.pedolu.smkcodingchallenge2.dao.UserDao
import com.pedolu.smkcodingchallenge2.data.model.room.UserModel

class UserRepository(private val userDao: UserDao, private val key: String) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.

    val user: LiveData<UserModel> = userDao.getUser(key)

    suspend fun insert(user: UserModel) {
        userDao.insert(user)
    }

    suspend fun update(user: UserModel) {
        userDao.update(user)
    }

    suspend fun delete(user: UserModel) {
        userDao.delete(user)
    }
}
