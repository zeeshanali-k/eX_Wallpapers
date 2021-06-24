package com.techsensei.exwallpapers.db

import android.content.Context
import androidx.room.*
import com.techsensei.exwallpapers.models.Wallpaper
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Database(entities = [Wallpaper::class], exportSchema = false, version = 1)
abstract class WallpapersDatabase :RoomDatabase(){
    abstract fun wallpapersDao():WallpapersDAO
    companion object{
        private const val NUMBER_OF_THREADS = 4
        val databaseWriteExecutor: ExecutorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS)
        private const val DATABASE_NAME="favouritewallpapers.db"
        @Volatile
        private var INSTANCE:WallpapersDatabase?=null
        fun getDatabase(context: Context): WallpapersDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext,
                        WallpapersDatabase::class.java, DATABASE_NAME)
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

}