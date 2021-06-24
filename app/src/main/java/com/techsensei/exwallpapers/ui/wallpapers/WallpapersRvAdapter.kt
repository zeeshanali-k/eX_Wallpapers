package com.techsensei.exwallpapers.ui.wallpapers

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.techsensei.exwallpapers.databinding.WallpapersRvLayoutBinding
import com.techsensei.exwallpapers.models.Wallpaper
import com.techsensei.exwallpapers.utils.PrefManager
import com.techsensei.exwallpapers.utils.onRvClickListener

class WallpapersRvAdapter(
    private var wallpapers: List<Wallpaper>,
    private val listener: onRvClickListener
): RecyclerView.Adapter<WallpapersRvAdapter.WallpapersHolder>() {
    private val isFav = Array(wallpapers.size,init = {false})

    init {
        for ((i, wallpaper:Wallpaper) in wallpapers.withIndex()){
            isFav[i]=wallpaper.isFavourite
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WallpapersHolder {
        return WallpapersHolder(WallpapersRvLayoutBinding
            .inflate(LayoutInflater.from(parent.context),
        parent,false))
    }

    private val TAG = "WallpapersRvAdapter"
    override fun onBindViewHolder(holder: WallpapersHolder, position: Int) {
        Log.d(TAG,wallpapers[position].link!!)
        holder.rvBinding.wallpaper= wallpapers[position]
        holder.rvBinding.wallpapersRvImg.setOnClickListener{listener.onItemClicked(position)}
        holder.rvBinding.isFav=isFav[position]
        //setting up favourite
        holder.rvBinding.wallpapersRvFav.setOnClickListener{
            holder.rvBinding.isFav=!isFav[position]
            isFav[position]=!isFav[position]
            listener.onFavAdded(position)
        }

//        setting up download
        holder.rvBinding.wallpapersRvDownloadBtn.setOnClickListener {

            listener.onRvDownloadBtnClicked(position)
        }

        holder.rvBinding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return wallpapers.size
    }

    fun updateWallpapers(wallpapers: List<Wallpaper>) {
        this.wallpapers=wallpapers
        notifyDataSetChanged()
    }

    class WallpapersHolder(val rvBinding: WallpapersRvLayoutBinding) : RecyclerView.ViewHolder(rvBinding.root)
}
