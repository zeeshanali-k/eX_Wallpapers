package com.techsensei.exwallpapers.ui.categoryDetail

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.techsensei.exwallpapers.db.WallpapersDAO
import com.techsensei.exwallpapers.db.WallpapersDatabase
import com.techsensei.exwallpapers.models.Wallpaper
import com.techsensei.exwallpapers.network.ApiClientProvider
import com.techsensei.exwallpapers.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryDetailViewModel(application: Application) : AndroidViewModel(application) {

    val wallpapersLiveData: MutableLiveData<List<Wallpaper>> = MutableLiveData()
    val wallpapersDBLiveData: MutableLiveData<List<Wallpaper>> = MutableLiveData()

    private val favsHandler: WallpapersDAO = WallpapersDatabase.getDatabase(application.applicationContext)
            .wallpapersDao()

    init {
        WallpapersDatabase.databaseWriteExecutor.execute {
            wallpapersDBLiveData.postValue(favsHandler.getFavWallpapers())
        }

    }


    fun getWallpapersByCategory(category:String){
        WallpapersDatabase.databaseWriteExecutor.execute {
            wallpapersLiveData.postValue(favsHandler.getWallpapersByCategory(category))
        }
    }

    fun addFav(wallpaper: Wallpaper) {
        WallpapersDatabase.databaseWriteExecutor.execute {
            favsHandler.addToFavourite(wallpaper)
        }
    }

    fun removeFav(wallpaper: Wallpaper){
        WallpapersDatabase.databaseWriteExecutor.execute {
            favsHandler.addToFavourite(wallpaper)
        }
    }
}