package com.example.walify.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModel
import com.example.walify.repository.MainRepository
import com.example.walify.model.Photo
import com.example.walify.model.PixelsResponse
import kotlinx.coroutines.launch

class MainViewModel(private val repository: MainRepository) : ViewModel() {

    private val _response = MutableLiveData<PixelsResponse>()

    val apiResponse: LiveData<PixelsResponse> get() = _response

    fun wallpapers(apiKey: String, query: String, perPage: Int) {
        viewModelScope.launch {
            val apiResponse = repository.getWallpapers(apiKey, query, perPage)
            _response.value = apiResponse
        }
    }
}