package com.example.walify.repository

import android.content.Context
import com.example.walify.dataBase.AppDatabase
import com.example.walify.dataBase.savePhoto

class SavedRepository(private var context: Context) {

    suspend fun getSavedPhotos(): List<savePhoto> {
        val photoDao = AppDatabase.getDataBase(context).photoDao()
        return photoDao.getAllPhotos()
    }
}