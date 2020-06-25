package com.pedolu.smkcodingchallenge2.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedolu.smkcodingchallenge2.data.model.room.UserModel
import com.pedolu.smkcodingchallenge2.db.UserDatabase
import com.pedolu.smkcodingchallenge2.repo.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    private lateinit var repository: UserRepository
    lateinit var user: LiveData<UserModel>

    fun init(context: Context, key: String) {
        val userDao = UserDatabase.getDatabase(context).UserDao()
        repository = UserRepository(userDao, key)
        user = repository.user
    }

    fun addData(user: UserModel) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(user)
    }

    fun deleteData(user: UserModel) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(user)
    }

    fun updateData(user: UserModel) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(user)
    }

}
