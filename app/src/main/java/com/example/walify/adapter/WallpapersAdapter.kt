package com.example.walify.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.walify.databinding.ItemPhotoBinding
import com.example.walify.model.Photo
import com.example.walify.ui.PreViewActivity
import com.example.walify.utils.WALLPAPER_OBJ

class WallpapersAdapter(private val photos: List<Photo>) :
    RecyclerView.Adapter<WallpapersAdapter.PhotoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) =
        holder.bind(photos[position])

    override fun getItemCount(): Int = photos.size

    class PhotoViewHolder(private var binding: ItemPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: Photo) {
            binding.photographerTextView.text = photo.photographer
            binding.root.context?.let { context ->

                Glide.with(context).load(photo.src.original).into(binding.photoImageView)

                binding.root.setOnClickListener {
                    context.startActivity(
                        Intent(context, PreViewActivity::class.java).putExtra(WALLPAPER_OBJ, photo)
                    )
                }
            }
        }
    }
}
