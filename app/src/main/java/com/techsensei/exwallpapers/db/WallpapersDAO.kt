package com.techsensei.exwallpapers.db

import androidx.room.*
import com.techsensei.exwallpapers.models.Wallpaper

@Dao
interface WallpapersDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addToFavourite(wallpaper:Wallpaper)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateFavourite(wallpaper:Wallpaper)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllWallpapers(wallpapers:List<Wallpaper>)

    @Query("SELECT * FROM wallpapers WHERE isFavourite=1 ORDER BY date DESC")
    fun getFavWallpapers():List<Wallpaper>

    @Query("SELECT * FROM wallpapers ORDER BY id DESC")
    fun getAllWallpapers():List<Wallpaper>

    @Query("SELECT * FROM wallpapers WHERE category=:category ORDER BY id DESC")
    fun getWallpapersByCategory(category:String):List<Wallpaper>

    @Query("SELECT COUNT(*) FROM wallpapers")
    fun getWallpapersCount():Int

}