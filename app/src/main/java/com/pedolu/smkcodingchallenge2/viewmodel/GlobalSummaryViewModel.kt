package com.pedolu.smkcodingchallenge2.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedolu.smkcodingchallenge2.data.model.room.GlobalSummaryModel
import com.pedolu.smkcodingchallenge2.db.GlobalSummaryDatabase
import com.pedolu.smkcodingchallenge2.repo.GlobalSummaryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GlobalSummaryViewModel : ViewModel() {

    private lateinit var repository: GlobalSummaryRepository
    lateinit var globalSummary: LiveData<GlobalSummaryModel>

    fun init(context: Context) {
        val globalSummaryDao = GlobalSummaryDatabase.getDatabase(context).GlobalSummaryDao()
        repository = GlobalSummaryRepository(globalSummaryDao)
        globalSummary = repository.globalSummary
    }

    fun addData(globalSummary: GlobalSummaryModel) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(globalSummary)
    }

    fun deleteData(globalSummary: GlobalSummaryModel) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(globalSummary)
    }

    fun updateData(globalSummary: GlobalSummaryModel) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(globalSummary)
    }

}
