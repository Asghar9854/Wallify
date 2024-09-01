package com.example.walify.network

import com.example.walify.model.PixelsResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface WallpapersApi {

    @GET("search")
    suspend fun searchWallpapers(
        @Header("Authorization") apiKey: String,
        @Query("query") query: String,
        @Query("per_page") perPage: Int
    ): PixelsResponse
}