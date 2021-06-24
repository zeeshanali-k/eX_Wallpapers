package com.techsensei.exwallpapers.ui.likes

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.techsensei.exwallpapers.R
import com.techsensei.exwallpapers.models.Wallpaper
import com.techsensei.exwallpapers.ui.preview.PreviewActivity
import com.techsensei.exwallpapers.ui.wallpapers.WallpapersViewModel
import com.techsensei.exwallpapers.utils.Constants
import com.techsensei.exwallpapers.utils.getDestination
import com.techsensei.exwallpapers.utils.onRvClickListener
import java.util.*

class LikesFrag : Fragment(), onRvClickListener {

    private lateinit var likedViewModel: WallpapersViewModel
    private lateinit var wallpapers: List<Wallpaper>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_likes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView=view.findViewById<RecyclerView>(R.id.liked_frag_rv)
        likedViewModel =
                ViewModelProvider(requireActivity()).get(WallpapersViewModel::class.java)
        likedViewModel.wallpapersDBLiveData.observe(viewLifecycleOwner,  {
            var rvAdapter: LikesFragRvAdapter?=null
            recyclerView?.adapter?.let { adapter: RecyclerView.Adapter<*> ->
                rvAdapter=adapter as LikesFragRvAdapter
            }

            if (it.isNotEmpty() && rvAdapter==null){
                wallpapers = it
                rvAdapter = LikesFragRvAdapter(it as MutableList<Wallpaper>,this)
                recyclerView.layoutManager=GridLayoutManager(requireActivity().applicationContext,
                2)
                recyclerView.adapter=rvAdapter
                recyclerView.itemAnimator=DefaultItemAnimator()
            }else if (rvAdapter!=null){
                rvAdapter!!.updateFavourites(it)
            }
        })
        likedViewModel.getFavWallpapers()
    }


    override fun onItemClicked(pos: Int) {
        val bundle=Bundle(2)
        bundle.putParcelable(Constants.WALLPAPER_DATA, wallpapers[pos])
        bundle.putString(Constants.CATEGORY_KEY,wallpapers[pos].category)
        val previewIntent=Intent(requireContext(),PreviewActivity::class.java)
        previewIntent.putExtra(Constants.WALLPAPER_DATA,wallpapers[pos])
        previewIntent.putExtra(Constants.CATEGORY_KEY,wallpapers[pos].category)
        startActivity(previewIntent)
    }

    override fun onFavAdded(pos: Int) {

        wallpapers[pos].date = Date().time
        likedViewModel.manageFav(wallpapers[pos])
    }

    private val TAG = "LikesFrag"
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
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P)
            downloadReq.allowScanningByMediaScanner()
        dm.enqueue(downloadReq)

        Toast.makeText(
            requireActivity().applicationContext, "Downloading...",
            Toast.LENGTH_SHORT
        ).show()

    }
}