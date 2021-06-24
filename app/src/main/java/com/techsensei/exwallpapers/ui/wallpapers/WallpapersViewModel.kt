package com.techsensei.exwallpapers.ui.wallpapers

import android.app.Application
import android.util.Log
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.techsensei.exwallpapers.db.WallpapersDAO
import com.techsensei.exwallpapers.db.WallpapersDatabase
import com.techsensei.exwallpapers.models.Wallpaper
import com.techsensei.exwallpapers.network.ApiClientProvider
import com.techsensei.exwallpapers.utils.OnDatabaseInsertedListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class WallpapersViewModel(application: Application) : AndroidViewModel(application) {

    val wallpapersLiveData: MutableLiveData<List<Wallpaper>> = MutableLiveData()
    val wallpapersDBLiveData: MutableLiveData<List<Wallpaper>> = MutableLiveData()
    private lateinit var listener: OnDatabaseInsertedListener

    private val wallpapersDAO: WallpapersDAO =
        WallpapersDatabase.getDatabase(application.applicationContext)
            .wallpapersDao()

    fun getFavWallpapers() {
        viewModelScope.launch(Dispatchers.IO) {
            wallpapersDBLiveData.postValue(wallpapersDAO.getFavWallpapers())
        }
    }

    fun getAllWallpapers() {
        WallpapersDatabase.databaseWriteExecutor.execute {
            wallpapersLiveData.postValue(wallpapersDAO.getAllWallpapers())
        }
    }

    fun manageFav(wallpaper: Wallpaper, isWallpapersFrag: Boolean = false) {
        WallpapersDatabase.databaseWriteExecutor.execute {
            Log.d(TAG, "manageFav: "+wallpaper.isFavourite)
            if (!wallpaper.isFavourite) {
                wallpaper.isFavourite = true
                wallpapersDAO.addToFavourite(wallpaper)
            }
            else {
                wallpapersDAO.deleteFavWallpaper(wallpaper)
                wallpaper.isFavourite=false
            }
            wallpapersDBLiveData.postValue(wallpapersDAO.getFavWallpapers())
            if (!isWallpapersFrag) {
                val tempWallpapers=wallpapersLiveData.value
                for ((i,tempWallpaper) in tempWallpapers!!.withIndex()){
                    if (wallpaper.id==tempWallpaper.id){
                        tempWallpapers[i].isFavourite=wallpaper.isFavourite
                        break
                    }
                }
                wallpapersLiveData.postValue(tempWallpapers)
            }
        }
    }

    private val TAG = "WallpapersViewModel"

    fun makeWallpapersReq() {
        ApiClientProvider.getApiClient().getWallpapers()
            .enqueue(object : Callback<List<Wallpaper>> {
                override fun onResponse(
                    @NonNull call: Call<List<Wallpaper>>,
                    @NonNull response: Response<List<Wallpaper>>
                ) {
                    Log.d(TAG, "onResponse: ")
                    if (response.isSuccessful && response.body() != null) {
                        Log.d(TAG, "onResponse: " + response.body()!!.size)
                        wallpapersLiveData.postValue(response.body())
                    } else {
                        wallpapersLiveData.postValue(null)
                    }
                }

                override fun onFailure(call: Call<List<Wallpaper>>, t: Throwable) {
                    Log.d(TAG, "onFailure: " + t.cause)
                    wallpapersLiveData.postValue(null)
                }
            })
    }

    fun setListener(listener: OnDatabaseInsertedListener) {
        this.listener = listener
    }

    suspend fun insertAllWallpapers(wallpapers: List<Wallpaper>) {
        wallpapersDAO.insertAllWallpapers(wallpapers)
        listener.onDataAdded()
    }

    fun addWallpapersToDatabase(favs: List<Wallpaper>, it: List<Wallpaper>) {
        viewModelScope.launch {
            //                setting date
            for ((i, wallpaper) in it.withIndex()) {
                var time = SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.getDefault())
                    .parse(wallpaper.created!!)!!.time
                if (time < 0) time *= -1
                it[i].date =
                    time

                if (favs != null && favs.isNotEmpty()) {
//                setting liked wallpapers
                    if (favContainsWallpaper(wallpaper, favs)) {
                        Log.d(TAG, "insertDataToDatabase: true")
                        it[i].isFavourite = true
                    }
                }
            }
            insertAllWallpapers(it)
        }

    }


    private fun favContainsWallpaper(wallpaper: Wallpaper, favs: List<Wallpaper>): Boolean {
        for (favWallpaper in favs) {
            Log.d(TAG, " ${favWallpaper.id} : ${wallpaper.id}")
            if (favWallpaper.id == wallpaper.id)
                return true
        }
        return false
    }

}