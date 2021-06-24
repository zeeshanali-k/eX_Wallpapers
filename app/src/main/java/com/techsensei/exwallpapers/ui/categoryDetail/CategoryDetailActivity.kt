package com.techsensei.exwallpapers.ui.categoryDetail

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.techsensei.exwallpapers.R
import com.techsensei.exwallpapers.databinding.CategoryDetailFragmentBinding
import com.techsensei.exwallpapers.models.Wallpaper
import com.techsensei.exwallpapers.ui.preview.PreviewActivity
import com.techsensei.exwallpapers.utils.Constants
import com.techsensei.exwallpapers.utils.getDestination
import com.techsensei.exwallpapers.utils.onRvClickListener
import java.util.*

class CategoryDetailActivity : AppCompatActivity(), onRvClickListener {

    private lateinit var viewModel: CategoryDetailViewModel
    private lateinit var category: String
    private lateinit var wallpapers: List<Wallpaper>
    private lateinit var categoryBinding: CategoryDetailFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryBinding = DataBindingUtil.setContentView(this, R.layout.category_detail_fragment)
        categoryBinding.categoryDetailTop.headerBackBtn
            .setOnClickListener { onBackPressed() }

        category = intent.extras!!.getString(Constants.CATEGORY_KEY).toString()
        categoryBinding.categoryDetailTop.title=category
        val recyclerView: RecyclerView = categoryBinding.detailsFragRv
        viewModel = ViewModelProvider(this).get(CategoryDetailViewModel::class.java)
        viewModel.wallpapersLiveData.observe(this, {
            //hiding progress bar
            categoryBinding.detailsFragPb.visibility = View.GONE

            if (it != null && it.isNotEmpty()) {
                wallpapers = it
                val rvAdapter = CategoryDetailRvAdapter(wallpapers, this)
                recyclerView.layoutManager = GridLayoutManager(
                    applicationContext,
                    2
                )
                recyclerView.adapter = rvAdapter
            }
        })
        viewModel.getWallpapersByCategory(category)
    }

    override fun onItemClicked(pos: Int) {
        val bundle = Bundle(2)
        bundle.putParcelable(Constants.WALLPAPER_DATA, wallpapers[pos])
        bundle.putString(Constants.CATEGORY_KEY, wallpapers[pos].category)
        val previewIntent = Intent(applicationContext, PreviewActivity::class.java)
        previewIntent.putExtra(Constants.WALLPAPER_DATA, wallpapers[pos])
        previewIntent.putExtra(Constants.CATEGORY_KEY, wallpapers[pos].category)
        startActivity(previewIntent)
    }

    override fun onFavAdded(pos: Int) {
        wallpapers[pos].date = Date().time
        if (wallpapers[pos].isFavourite) {
            wallpapers[pos].isFavourite = !wallpapers[pos].isFavourite
            viewModel.removeFav(wallpapers[pos])
        } else {
            wallpapers[pos].isFavourite = !wallpapers[pos].isFavourite
            viewModel.addFav(wallpapers[pos])
        }
    }

    private val TAG = "CategoryDetailFrag"

    override fun onRvDownloadBtnClicked(pos: Int) {
        val destination =
            getDestination(wallpapers[pos].name!!, applicationContext)
        Log.d(TAG, "onRvDownloadBtnClicked: $destination")
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


        val dm: DownloadManager =
            getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadReq = DownloadManager.Request(wallpapers[pos].link!!.toUri())
            .setTitle(wallpapers[pos].name)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            .setDestinationUri(destination)
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P)
            downloadReq.allowScanningByMediaScanner()
        dm.enqueue(downloadReq)

        Toast.makeText(
            applicationContext, "Downloading...",
            Toast.LENGTH_SHORT
        ).show()

    }

}