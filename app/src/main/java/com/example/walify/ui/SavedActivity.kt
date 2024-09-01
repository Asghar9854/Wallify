package com.example.walify.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.walify.databinding.ActivitySavedBinding
import com.example.walify.viewmodel.SavedViewModel

class SavedActivity : AppCompatActivity() {
    var binding: ActivitySavedBinding? = null
    private val savedViewModel  by viewModels<SavedViewModel>()/* {
        SavedViewModelFactory(SavedRepository(this))
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedBinding.inflate(layoutInflater)
        setContentView(binding?.root!!)
        savedViewModel.getSavedPhotos(this)
        // Observe the photos LiveData
        savedViewModel.savedPhotos.observe(this, Observer { response ->
            Log.d("TAG", "savedPhotos: ${response?.size}")
        })
        //
    }
}