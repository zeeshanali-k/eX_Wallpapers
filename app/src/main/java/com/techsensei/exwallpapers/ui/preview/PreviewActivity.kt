package com.techsensei.exwallpapers.ui.preview

import android.app.DownloadManager
import android.app.WallpaperManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.N
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso
import com.techsensei.exwallpapers.R
import com.techsensei.exwallpapers.databinding.FragmentPreviewBinding
import com.techsensei.exwallpapers.models.Wallpaper
import com.techsensei.exwallpapers.utils.Constants
import com.techsensei.exwallpapers.utils.getDestination
import java.io.FileInputStream
//TODO search and category list design
class PreviewActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var setWallpaperBtn: Button
    private var isDownloaded: Boolean = false
    private var setWallpaper: Boolean = false
    private lateinit var destination: Uri

    private val downloadReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {
                downloadBtn.isEnabled = true
                setWallpaperBtn.isEnabled = true
                isDownloaded = true

                if (setWallpaper) setWallpaper()

                Toast.makeText(context, "Wallpaper Downloaded!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private lateinit var wallpaper: Wallpaper
    private lateinit var previewBinding: FragmentPreviewBinding
    private lateinit var downloadBtn: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        previewBinding=DataBindingUtil.setContentView(this,R.layout.fragment_preview)
        previewBinding.categoryDetailTop.headerBackBtn
            .setOnClickListener { onBackPressed() }
        registerReceiver(
            downloadReceiver,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )

        val extras = intent.extras!!

        var wallpaperCategory: String?
        extras.apply {
            wallpaper= getParcelable(Constants.WALLPAPER_DATA)!!
            wallpaperCategory=getString(Constants.CATEGORY_KEY)
        }

        previewBinding.title=wallpaperCategory

        downloadBtn = findViewById(R.id.preview_download_btn)
        setWallpaperBtn = findViewById(R.id.set_wallpaper_btn)
        downloadBtn.setOnClickListener(this)
        setWallpaperBtn.setOnClickListener(this)

        val previewImg = findViewById<ImageView>(R.id.preview_wallpaper)
        Glide.with(applicationContext)
            .load(wallpaper.thumbnail)
            .centerCrop()
            .placeholder(R.drawable.ph)
            .into(previewImg)
    }
    
    override fun onClick(v: View) {
        if (v.id == R.id.preview_download_btn) {
            downloadWallpaper()
        } else {
            setWallpaper()
        }
    }

    private fun setWallpaper() {
        if (!isDownloaded) {
            downloadWallpaper()
            setWallpaper = true
            return
        }

        val wm: WallpaperManager =
            getSystemService(WALLPAPER_SERVICE) as WallpaperManager
        if (SDK_INT >= N) {
            if (!wm.isSetWallpaperAllowed) {
                Toast.makeText(
                    applicationContext, "Cannot set wallpaper!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        /*if (SDK_INT > P)
            wm.setStream(contentResolver.openInputStream(destination))
        else*/
            wm.setStream(FileInputStream(destination.toFile()))
        Toast.makeText(
            applicationContext, "Wallpaper Set",
            Toast.LENGTH_SHORT
        ).show()
    }

    private val TAG = "PreviewFrag"
    private fun downloadWallpaper() {
        downloadBtn.isEnabled = false
        setWallpaperBtn.isEnabled = false
        destination =
            getDestination(wallpaper.name!!, applicationContext)
        Log.d(TAG, "onRvDownloadBtnClicked: $destination")
        Toast.makeText(
            applicationContext, "Downloading...",
            Toast.LENGTH_SHORT
        ).show()

        val dm: DownloadManager =
            getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadReq = DownloadManager.Request(wallpaper.link!!.toUri())
            .setTitle(wallpaper.name)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            .setDestinationUri(destination)
        if (SDK_INT <= Build.VERSION_CODES.P)
            downloadReq.allowScanningByMediaScanner()
        dm.enqueue(downloadReq)

        Toast.makeText(
            applicationContext, "Downloading...",
            Toast.LENGTH_SHORT
        ).show()

/*

        Glide.with(this)
            .asBitmap()
            .load(wallpapers[pos].link)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(bitmap: Bitmap, transition: Transition<in Bitmap>?) {
                    val outputStream = getOutputStream(destination)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    outputStream.close()
                    isDownloading[pos] = false
                    rvAdapter.notifyDataSetChanged()
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }
            })
*/

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(downloadReceiver)
    }

}