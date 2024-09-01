package com.example.walify.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.walify.dataBase.savePhoto
import com.example.walify.databinding.ActivityEditBinding
import com.example.walify.utils.WALLPAPER_OBJ
import com.example.walify.utils.urlToBitmap

class EditActivity : AppCompatActivity() {
    var binding: ActivityEditBinding? = null
    var photo: savePhoto? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding?.root!!)

        photo = intent.getSerializableExtra(WALLPAPER_OBJ) as? savePhoto
        binding?.imgvEdit?.let {
            Glide.with(this@EditActivity).load(photo?.imagePath).into(it)
        }

        binding?.btnFilter?.setOnClickListener {
            photo?.let {
                urlToBitmap(
                    it.imagePath,
                    this@EditActivity,
                    onSuccess = { bitmap ->
                        binding?.imgvEdit?.setImageBitmap(bitmap)
                    },
                    onError = {})
            }
        }
    }
}