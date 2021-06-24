package com.techsensei.exwallpapers.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import com.techsensei.exwallpapers.R

    @BindingAdapter("setWallpaper")
    fun setIvWallpaper(imageView:ImageView,url:String){
        Glide.with(imageView.context)
                .load(url)
                .placeholder(R.drawable.ph)
                .into(imageView);
        }

    @BindingAdapter("setFavourite")
    fun setFavourite(imageView:ImageView,isFav:Boolean){
            if (isFav) imageView.setImageResource(R.drawable.ic_baseline_favorite_24)
            else imageView.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }

