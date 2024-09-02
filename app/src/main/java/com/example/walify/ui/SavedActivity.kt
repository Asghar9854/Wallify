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
    private var binding: ActivitySavedBinding? = null
    private val savedViewModel by viewModels<SavedViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedBinding.inflate(layoutInflater)
        setContentView(binding?.root!!)


        savedViewModel.getSavedPhotos(this)?.observe(this) {
            val savedAdapter = it?.let { SavedAdapter(it) }
            binding?.recyclerview?.adapter = savedAdapter
        }

    }
}