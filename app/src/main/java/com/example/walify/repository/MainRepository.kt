package com.example.walify.repository

import com.example.walify.model.PixelsResponse
import com.example.walify.network.RetrofitInstance

class MainRepository {

    suspend fun getWallpapers(apiKey: String, query: String, perPage: Int): PixelsResponse {
        return RetrofitInstance.api.searchWallpapers(apiKey, query, perPage)
    }
}
