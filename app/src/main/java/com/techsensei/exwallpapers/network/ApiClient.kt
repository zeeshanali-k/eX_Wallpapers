package com.techsensei.exwallpapers.network

import com.techsensei.exwallpapers.models.Wallpaper
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiClient {

    @GET("get-wallpapers.php")
    fun getWallpapers(): Call<List<Wallpaper>>

    @GET("get-wallpapers-by-category.php")
    fun getWallpapersByCategory(
//        @Query("auth") auth: String,
        @Query("category") category: String
    ): Call<List<Wallpaper>>

}