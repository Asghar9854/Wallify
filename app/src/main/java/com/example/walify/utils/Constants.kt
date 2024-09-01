package com.example.walify.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.animation.Transformation
import android.widget.Toast
import coil.ImageLoader
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream


var pixelApiKey = "9ybqmcDFACoAEsaStiwcLT2XRKkT0Sxww4XpWgBX8cEzRgrxMsmDPlle"

var WALLPAPER_OBJ = "wallpaperObj"


fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Activity.getBitmapFromUrl(imageUrl: String): Bitmap? {
    return Glide.with(this)
        .asBitmap()
        .load(imageUrl)
        .submit()
        .get()
}


fun urlToBitmap(
    imageURL: String,
    context: Context,
    transformation: Transformation? = null,
    onSuccess: (bitmap: Bitmap) -> Unit,
    onError: (error: Throwable) -> Unit
) {
    var bitmap: Bitmap? = null
    val loadBitmap = CoroutineScope(Dispatchers.IO).launch {
        val loader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(imageURL)
            //.transformations(transformation?:DefaultTransformation())
            .allowHardware(false)
            .build()
        val result = loader.execute(request)
        if (result is SuccessResult) {
            bitmap = (result.drawable as BitmapDrawable).bitmap
        } else if (result is ErrorResult) {
            cancel(result.throwable.localizedMessage ?: "ErrorResult", result.throwable)
        }
    }
    loadBitmap.invokeOnCompletion { throwable ->
        CoroutineScope(Dispatchers.Main).launch {
            bitmap?.let {
                onSuccess(it)
            } ?: throwable?.let {
                onError(it)
            } ?: onError(Throwable("Undefined Error"))
        }
    }
}


fun Context.saveImageToCacheStorage(bitmap: Bitmap): String {
    val file = File(cacheDir, "walify_${System.currentTimeMillis()}.png")
    val fileOutputStream = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
    fileOutputStream.flush()
    fileOutputStream.close()
    return file.path
}