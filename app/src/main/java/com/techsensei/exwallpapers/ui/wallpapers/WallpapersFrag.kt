package com.techsensei.exwallpapers.ui.wallpapers

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.techsensei.exwallpapers.R
import com.techsensei.exwallpapers.models.Wallpaper
import com.techsensei.exwallpapers.ui.preview.PreviewActivity
import com.techsensei.exwallpapers.utils.Constants
import com.techsensei.exwallpapers.utils.getDestination
import com.techsensei.exwallpapers.utils.onRvClickListener
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*

class WallpapersFrag : Fragment(), onRvClickListener {

    private lateinit var rvAdapter: WallpapersRvAdapter
    private lateinit var wallpapersViewModel: WallpapersViewModel
    private lateinit var wallpapers: List<Wallpaper>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_wallpapers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = view.findViewById(R.id.wallpapers_frag_rv)
        wallpapersViewModel =
            ViewModelProvider(requireActivity()).get(WallpapersViewModel::class.java)
        //getting wallpapers from network
        wallpapersViewModel.wallpapersLiveData.observe(viewLifecycleOwner, {
            //hiding progress bar
            view.findViewById<ProgressBar>(R.id.wallpapers_frag_pb)
                .visibility = View.GONE

            if (it != null && it.isNotEmpty()) {
                wallpapers = it
//                if (recyclerView.adapter==null) {
                var lastPos = -1
                if (recyclerView.layoutManager != null)
                    lastPos =
                        (recyclerView.layoutManager as GridLayoutManager).findLastVisibleItemPosition()
                rvAdapter = WallpapersRvAdapter(wallpapers, this)
                recyclerView.layoutManager = GridLayoutManager(
                    requireActivity().applicationContext,
                    2
                )
                recyclerView.adapter = rvAdapter
                if (lastPos >= 0)
                    recyclerView.scrollToPosition(lastPos)
//                }else{
//                    rvAdapter.updateWallpapers(wallpapers)
//                }
            }
        })
        wallpapersViewModel.getAllWallpapers()
    }

    override fun onItemClicked(pos: Int) {
        val bundle = Bundle(2)
        bundle.putParcelable(Constants.WALLPAPER_DATA, wallpapers[pos])
        bundle.putString(Constants.CATEGORY_KEY, wallpapers[pos].category)
        val previewIntent = Intent(requireContext(), PreviewActivity::class.java)
        previewIntent.putExtra(Constants.WALLPAPER_DATA, wallpapers[pos])
        previewIntent.putExtra(Constants.CATEGORY_KEY, wallpapers[pos].category)
        startActivity(previewIntent)
    }

    override fun onFavAdded(pos: Int) {
        wallpapers[pos].date = Date().time
        wallpapersViewModel.manageFav(wallpapers[pos], true)
    }

    private val TAG = "WallpapersFrag"

    override fun onRvDownloadBtnClicked(pos: Int) {
        val destination =
            getDestination(wallpapers[pos].name!!, requireActivity().applicationContext)
        Log.d(TAG, "onRvDownloadBtnClicked: $destination")
        Toast.makeText(
            requireActivity().applicationContext, "Downloading...",
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
            requireActivity().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadReq = DownloadManager.Request(wallpapers[pos].link!!.toUri())
            .setTitle(wallpapers[pos].name)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            .setDestinationUri(destination)
        if (SDK_INT <= Build.VERSION_CODES.P)
            downloadReq.allowScanningByMediaScanner()
        dm.enqueue(downloadReq)

        Toast.makeText(
            requireActivity().applicationContext, "Downloading...",
            Toast.LENGTH_SHORT
        ).show()

    }

    private fun getOutputStream(destination: Uri): OutputStream {
        return if (SDK_INT >= 29) {
            requireContext().contentResolver.openOutputStream(destination)!!
        } else {
            FileOutputStream(File(destination.toString()))
        }
    }

}