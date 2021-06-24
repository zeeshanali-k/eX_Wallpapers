package com.techsensei.exwallpapers.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.transition.TransitionManager
import com.techsensei.exwallpapers.MainActivity
import com.techsensei.exwallpapers.R
import com.techsensei.exwallpapers.databinding.ActivitySplashBinding
import com.techsensei.exwallpapers.db.WallpapersDatabase
import com.techsensei.exwallpapers.models.Wallpaper
import com.techsensei.exwallpapers.ui.wallpapers.WallpapersViewModel
import com.techsensei.exwallpapers.utils.OnDatabaseInsertedListener
import com.techsensei.exwallpapers.utils.constants.PrefConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext

class SplashActivity : AppCompatActivity(), View.OnClickListener ,OnDatabaseInsertedListener{
    private lateinit var splashBinding: ActivitySplashBinding
    private lateinit var wallpapersViewModel: WallpapersViewModel
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var todayDate: String
    private val TAG = "SplashActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        todayDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            .format(Date())
        sharedPreferences = getSharedPreferences(PrefConstants.PREF_NAME, MODE_PRIVATE)
//        checking date
        if (sharedPreferences.getString(PrefConstants.DATE_PREF, null) == null ||
            !sharedPreferences.getString(PrefConstants.DATE_PREF, null).equals( todayDate)
        ) {
            wallpapersViewModel = ViewModelProvider(this)
                .get(WallpapersViewModel::class.java)
            wallpapersViewModel.wallpapersLiveData.observe(this, {
                if (it != null && it.isNotEmpty()) {
//                    Inserting to database
                    wallpapersViewModel.wallpapersDBLiveData.observe(this, {favs->
                        wallpapersViewModel.addWallpapersToDatabase(favs, it)
                    })
                    wallpapersViewModel.getFavWallpapers()
                } else {
                    TransitionManager.beginDelayedTransition(splashBinding.root as ViewGroup)
                    splashBinding.isRetry = true
                    Toast.makeText(
                        applicationContext, "Failed to get data. Try again!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
            wallpapersViewModel.setListener(this)
            wallpapersViewModel.makeWallpapersReq()
        } else {
            splashBinding.splashLoading.pauseAnimation()
            splashBinding.isNotLoading=true
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            }, 1000)
        }
    }


    override fun onClick(v: View?) {
        splashBinding.reloadBtn.animate()
            .scaleX(0F)
            .scaleY(0F)
            .setDuration(300)
            .rotation(360F)
            .withEndAction {
                splashBinding.isRetry = false
                wallpapersViewModel.makeWallpapersReq()
            }
    }

    override fun onDataAdded() {
        sharedPreferences.edit().putString(PrefConstants.DATE_PREF,todayDate)
            .apply()
        startActivity(Intent(applicationContext, MainActivity::class.java))
        finish()
    }


}