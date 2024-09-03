package com.example.walify.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import com.example.walify.adapter.WallpapersAdapter
import com.example.walify.databinding.ActivityMainBinding
import com.example.walify.utils.hide
import com.example.walify.utils.isNetworkAvailable
import com.example.walify.utils.pixelApiKey
import com.example.walify.utils.show
import com.example.walify.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private var wallpapersAdapter: WallpapersAdapter? = null

    private val viewModel: MainViewModel by viewModels<MainViewModel>()
    var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root!!)

        if (isNetworkAvailable()) {
            binding?.tvnointernet?.hide()
            fetchApiData()
        } else {
            binding?.progressbar?.hide()
            binding?.tvnointernet?.show()
        }

        binding?.searchview?.let { searchView ->
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (!query.isNullOrEmpty()) {
                        binding?.progressbar?.visibility = View.VISIBLE
                        viewModel.wallpapers(pixelApiKey, query, 16)
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
        // Observe the photos LiveData

    }

    fun fetchApiData() {
        viewModel.apiResponse.observe(this, Observer { response ->
            binding?.progressbar?.visibility = View.GONE
            wallpapersAdapter = WallpapersAdapter(response.photos)
            binding?.recyclerview?.adapter = wallpapersAdapter
        })

        val apiKey = pixelApiKey
        val query = "nature"
        val perPage = 16
        viewModel.wallpapers(apiKey, query, perPage)
    }
}