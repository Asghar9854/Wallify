package com.example.walify.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.walify.adapter.SavedAdapter
import com.example.walify.adapter.WallpapersAdapter
import com.example.walify.databinding.ActivitySavedBinding
import com.example.walify.viewmodel.SavedViewModel

class SavedActivity : AppCompatActivity() {
    var binding: ActivitySavedBinding? = null
    private val savedViewModel by viewModels<SavedViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedBinding.inflate(layoutInflater)
        setContentView(binding?.root!!)
        savedViewModel.getSavedPhotos(this)
        // Observe the photos LiveData
        savedViewModel.savedPhotos.observe(this, Observer { response ->
            Log.d("TAG", "savedPhotos: ${response?.size}")
            val savedAdapter = response?.let { SavedAdapter(it) }
            binding?.recyclerview?.adapter = savedAdapter
        })
    }
}