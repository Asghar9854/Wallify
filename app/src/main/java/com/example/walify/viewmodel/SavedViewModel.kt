package com.example.walify.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.walify.repository.SavedRepository

class SavedViewModel : ViewModel() {
    private var repository: SavedRepository = SavedRepository()
    fun getSavedPhotos(context: Context) = repository.getSavedPhotos(context)
}