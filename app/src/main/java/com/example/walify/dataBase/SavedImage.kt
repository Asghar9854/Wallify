package com.example.walify.dataBase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_images")
data class savePhoto(
    @PrimaryKey val id: Int,
    val name: String,
    val imagePath: String
)
