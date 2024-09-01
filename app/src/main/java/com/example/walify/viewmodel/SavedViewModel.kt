package com.example.walify.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.walify.dataBase.savePhoto
import com.example.walify.repository.SavedRepository
import kotlinx.coroutines.launch

class SavedViewModel() : ViewModel() {
    private var repository: SavedRepository? = null
    private val _savedPhotos = MutableLiveData<List<savePhoto>?>()
    val savedPhotos: LiveData<List<savePhoto>?> get() = _savedPhotos

    fun getSavedPhotos(context: Context) {
        repository = SavedRepository(context)
        viewModelScope.launch {
            val savedPhotos = repository?.getSavedPhotos()
            _savedPhotos.value = savedPhotos
        }
    }

}