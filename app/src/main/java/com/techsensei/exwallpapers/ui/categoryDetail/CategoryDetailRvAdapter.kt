package com.techsensei.exwallpapers.ui.categoryDetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.techsensei.exwallpapers.databinding.WallpapersRvLayoutBinding
import com.techsensei.exwallpapers.models.Wallpaper
import com.techsensei.exwallpapers.utils.onRvClickListener

class CategoryDetailRvAdapter(private val wallpapers:List<Wallpaper>,
                              private val listener: onRvClickListener): RecyclerView.Adapter<CategoryDetailRvAdapter.CategoryHolder>() {
    private val isFav = Array(wallpapers.size,init = {false})

    init {
        for ((i, wallpaper:Wallpaper) in wallpapers.withIndex()){
            isFav[i]=wallpaper.isFavourite
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        return CategoryHolder(WallpapersRvLayoutBinding
            .inflate(LayoutInflater.from(parent.context),
                parent,false))
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        holder.rvBinding.wallpaper= wallpapers[position]
        holder.rvBinding.wallpapersRvImg.setOnClickListener{listener.onItemClicked(position)}
        holder.rvBinding.isFav=isFav[position]
        //setting up favourite
        holder.rvBinding.wallpapersRvFav.setOnClickListener{
            holder.rvBinding.isFav=!isFav[position]
            isFav[position]=!isFav[position]
            listener.onFavAdded(position)
        }

        holder.rvBinding.wallpapersRvDownloadBtn.setOnClickListener{
            listener.onRvDownloadBtnClicked(position)
        }

        holder.rvBinding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return wallpapers.size
    }

    class CategoryHolder(val rvBinding: WallpapersRvLayoutBinding) : RecyclerView.ViewHolder(rvBinding.root)
}
