package com.example.walify.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.walify.dataBase.AppDatabase
import com.example.walify.dataBase.savePhoto
import com.example.walify.databinding.ActivityPreViewBinding
import com.example.walify.model.Photo
import com.example.walify.utils.WALLPAPER_OBJ
import com.example.walify.utils.saveImageToCacheStorage
import com.example.walify.utils.showToast
import com.example.walify.utils.urlToBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PreViewActivity : AppCompatActivity() {
    private var binding: ActivityPreViewBinding? = null
    private var photo: Photo? = null
    var database: AppDatabase? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreViewBinding.inflate(layoutInflater)
        setContentView(binding?.root!!)
        database = AppDatabase.getDataBase(this@PreViewActivity)
        photo = intent.getSerializableExtra(WALLPAPER_OBJ) as? Photo
        binding?.imgpreview?.let {
            Glide.with(this@PreViewActivity).load(photo?.src?.large).into(it)
        }

        binding?.btnDownload?.setOnClickListener {
            photo?.let { objPhoto ->
                urlToBitmap(
                    objPhoto.src.original,
                    this@PreViewActivity,
                    onSuccess = { bitmap ->
                        val path = this@PreViewActivity.saveImageToCacheStorage(bitmap)
                        showToast("Image Saved Successfully")
                        insertDB(savePhoto(objPhoto.id, objPhoto.photographer, path))
                    },
                    onError = {})
            }
        }
    }

    private fun insertDB(savePhoto: savePhoto) {
        val photoDao = database?.photoDao()
        CoroutineScope(Dispatchers.IO).launch {
            photoDao?.insertPhoto(savePhoto)
        }
    }
}