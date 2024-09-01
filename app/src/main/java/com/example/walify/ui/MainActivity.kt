package com.example.walify.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import com.example.walify.adapter.WallpapersAdapter
import com.example.walify.databinding.ActivityMainBinding
import com.example.walify.repository.MainRepository
import com.example.walify.utils.pixelApiKey
import com.example.walify.viewmodel.MainViewModel
import com.example.walify.viewmodel.MainViewModelFactory

class MainActivity : AppCompatActivity() {

    private var wallpapersAdapter: WallpapersAdapter? = null

    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(MainRepository())
    }
    var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root!!)

        // Observe the photos LiveData
        viewModel.apiResponse.observe(this, Observer { response ->
            wallpapersAdapter = WallpapersAdapter(response.photos)
            binding?.recyclerview?.adapter = wallpapersAdapter
        })

        val apiKey = pixelApiKey
        val query = "nature"
        val perPage = 16

        viewModel.wallpapers(apiKey, query, perPage)

        binding?.searchview?.let { searchView ->
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (!query.isNullOrEmpty()) {
                        viewModel.wallpapers(apiKey, query, perPage)
                    }

                    searchView.setQuery("", false)
                    searchView.isIconified = true
                    searchView.clearFocus()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }
            })
        }

        binding?.btnsaved?.setOnClickListener {
            startActivity(Intent(this@MainActivity, SavedActivity::class.java))
        }
    }
}