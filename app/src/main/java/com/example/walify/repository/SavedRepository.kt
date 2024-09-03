package com.example.walify.repository

import android.content.Context
import com.example.walify.dataBase.AppDatabase

class SavedRepository {
    fun getSavedPhotos(context: Context) =
        AppDatabase.getDataBase(context).photoDao().getAllPhotos()
}