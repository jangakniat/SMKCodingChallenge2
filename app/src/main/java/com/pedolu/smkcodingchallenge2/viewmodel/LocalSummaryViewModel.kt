package com.pedolu.smkcodingchallenge2.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedolu.smkcodingchallenge2.data.model.room.LocalSummaryModel
import com.pedolu.smkcodingchallenge2.db.LocalSummaryDatabase
import com.pedolu.smkcodingchallenge2.repo.LocalSummaryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LocalSummaryViewModel : ViewModel() {

    private lateinit var repository: LocalSummaryRepository
    lateinit var localSummary: LiveData<LocalSummaryModel>

    fun init(context: Context, country: String) {
        val localSummaryDao = LocalSummaryDatabase.getDatabase(context).LocalSummaryDao()
        repository = LocalSummaryRepository(localSummaryDao, country)
        localSummary = repository.localSummary
    }

    fun addData(localSummary: LocalSummaryModel) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(localSummary)
    }

}