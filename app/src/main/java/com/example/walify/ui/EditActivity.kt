package com.example.walify.ui

import android.app.WallpaperManager
import android.graphics.Bitmap
import android.os.Bundle
import android.view.animation.Transformation
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.GraphicsLayerScope
import com.bumptech.glide.Glide
import com.example.walify.dataBase.AppDatabase
import com.example.walify.dataBase.savePhoto
import com.example.walify.databinding.ActivityEditBinding
import com.example.walify.model.Photo
import com.example.walify.utils.BlackWhiteTransformation
import com.example.walify.utils.BrightnessTransformation
import com.example.walify.utils.ContrastTransformation
import com.example.walify.utils.CoolTransformation
import com.example.walify.utils.DefaultTransformation
import com.example.walify.utils.GrayScaleTransformation
import com.example.walify.utils.InvertColorsTransformation
import com.example.walify.utils.SaturationTransformation
import com.example.walify.utils.SepiaTransformation
import com.example.walify.utils.VintageTransformation
import com.example.walify.utils.WALLPAPER_OBJ
import com.example.walify.utils.WarmthTransformation
import com.example.walify.utils.saveImageToCacheStorage
import com.example.walify.utils.showToast
import com.example.walify.utils.urlToBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditActivity : AppCompatActivity() {
    var binding: ActivityEditBinding? = null
    var photo: savePhoto? = null
    var imageBitmap: Bitmap? = null
    var database: AppDatabase? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding?.root!!)
        database = AppDatabase.getDataBase(this@EditActivity)
        photo = intent.getSerializableExtra(WALLPAPER_OBJ) as? savePhoto

        photo?.let {
            urlToBitmap(
                it.imagePath,
                this@EditActivity,
                transformation = DefaultTransformation(),
                onSuccess = { bitmap ->
                    imageBitmap = bitmap
                    binding?.imgvEdit?.setImageBitmap(bitmap)
                },
                onError = {})
        }

        binding?.btnDefaultFilter?.setOnClickListener {
            appFilter(DefaultTransformation())
        }
        binding?.btnSepiaFilter?.setOnClickListener {
            appFilter(SepiaTransformation())
        }
        binding?.btnBlackWhiteFilter?.setOnClickListener {
            appFilter(BlackWhiteTransformation())
        }
        binding?.btnGrayScaleFilter?.setOnClickListener {
            appFilter(GrayScaleTransformation())
        }
        binding?.btnContrastFilter?.setOnClickListener {
            appFilter(ContrastTransformation())
        }
        binding?.btnCoolFilter?.setOnClickListener {
            appFilter(CoolTransformation())
        }
        binding?.btnBrightFilter?.setOnClickListener {
            appFilter(BrightnessTransformation())
        }
        binding?.btnInvertFilter?.setOnClickListener {
            appFilter(InvertColorsTransformation())
        }
        binding?.btSaturationFilter?.setOnClickListener {
            appFilter(SaturationTransformation())
        }
        binding?.btnVintageFilter?.setOnClickListener {
            appFilter(VintageTransformation())
        }
        binding?.btnWarmFilter?.setOnClickListener {
            appFilter(WarmthTransformation())
        }

        binding?.btnSaveEdits?.setOnClickListener {
            val path = imageBitmap?.let { it1 -> this@EditActivity.saveImageToCacheStorage(it1) }
            showToast("Image Saved Successfully")
            photo?.let {
                path?.let { it1 -> savePhoto(it.id, it.name, it1) }
                    ?.let { it2 -> insertDB(it2) }
            }
        }

        binding?.btnApplyLock?.setOnClickListener {
            imageBitmap?.let { it1 ->
                com.example.walify.utils.setWallpaper(
                    this@EditActivity,
                    it1, WallpaperManager.FLAG_LOCK
                )
            }
        }
        binding?.btnAppyHOme?.setOnClickListener {
            imageBitmap?.let { it1 ->
                com.example.walify.utils.setWallpaper(
                    this@EditActivity,
                    it1, WallpaperManager.FLAG_SYSTEM
                )
            }
        }
    }

    private fun insertDB(savePhoto: savePhoto) {
        val photoDao = database?.photoDao()
        CoroutineScope(Dispatchers.IO).launch {
            photoDao?.insertPhoto(savePhoto)
        }
    }

    private fun appFilter(transformation: coil.transform.Transformation) {
        photo?.let {
            urlToBitmap(
                it.imagePath,
                this@EditActivity,
                transformation = transformation,
                onSuccess = { bitmap ->
                    imageBitmap = bitmap
                    binding?.imgvEdit?.setImageBitmap(bitmap)
                },
                onError = {})
        }
    }
}