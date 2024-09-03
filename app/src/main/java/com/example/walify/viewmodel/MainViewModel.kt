package com.example.walify.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.walify.model.PixelsResponse
import com.example.walify.repository.MainRepository
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val repository: MainRepository = MainRepository()

    private val _response = MutableLiveData<PixelsResponse>()
    val apiResponse: LiveData<PixelsResponse> get() = _response

    fun wallpapers(apiKey: String, query: String, perPage: Int) {
        viewModelScope.launch {
            _response.value = repository.getWallpapers(apiKey, query, perPage)
        }
    }
}