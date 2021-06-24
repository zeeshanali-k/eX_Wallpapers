package com.techsensei.exwallpapers.ui.likes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.techsensei.exwallpapers.databinding.WallpapersRvLayoutBinding
import com.techsensei.exwallpapers.models.Wallpaper
import com.techsensei.exwallpapers.utils.onRvClickListener

class LikesFragRvAdapter(private val wallpapers:MutableList<Wallpaper>,private val listener:onRvClickListener): RecyclerView.Adapter<LikesFragRvAdapter.LikedHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikedHolder {
        return LikedHolder(WallpapersRvLayoutBinding
            .inflate(LayoutInflater.from(parent.context),
                parent,false))
    }

    override fun onBindViewHolder(holder: LikedHolder, position: Int) {
        holder.rvBinding.wallpaper= wallpapers[position]

        holder.rvBinding.wallpapersRvImg.setOnClickListener{ listener.onItemClicked(position) }

        holder.rvBinding.isFav=wallpapers[position].isFavourite
        //setting up favourite
        holder.rvBinding.wallpapersRvFav.setOnClickListener{
            listener.onFavAdded(position)
        }

        holder.rvBinding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return wallpapers.size
    }

    fun updateFavourites(updated: List<Wallpaper>) {
        wallpapers.clear()
        wallpapers.addAll(updated)
        notifyDataSetChanged()
    }

    class LikedHolder(val rvBinding: WallpapersRvLayoutBinding) : RecyclerView.ViewHolder(rvBinding.root)
}
