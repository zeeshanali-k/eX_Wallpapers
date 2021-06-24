package com.techsensei.exwallpapers

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.techsensei.exwallpapers.databinding.ActivityMainBinding
import com.techsensei.exwallpapers.ui.category.CategoryFrag
import com.techsensei.exwallpapers.ui.likes.LikesFrag
import com.techsensei.exwallpapers.ui.wallpapers.WallpapersFrag
import com.techsensei.exwallpapers.utils.ZoomOutPageTransformer
import github.com.st235.lib_expandablebottombar.ExpandableBottomBar
import github.com.st235.lib_expandablebottombar.MenuItem
import github.com.st235.lib_expandablebottombar.MenuItemDescriptor
import java.util.*

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val navView: ExpandableBottomBar = mainBinding.navView
        val menu = navView.menu

        menu.add(
            MenuItemDescriptor.Builder(
                this,
                R.id.navigation_wallpapers,
                R.drawable.ic_baseline_wallpaper_24,
                R.string.title_wallpaper,
                ResourcesCompat.getColor(resources,R.color.tabSelectedItem,null)
            )
                .build()
        )

        menu.add(
            MenuItemDescriptor.Builder(
                this,
                R.id.navigation_category,
                R.drawable.ic_baseline_category_24,
                R.string.title_categories,
                ResourcesCompat.getColor(resources,R.color.tabSelectedItem,null)
            )
                .build()
        )
        menu.add(
            MenuItemDescriptor.Builder(
                this,
                R.id.navigation_likes,
                R.drawable.ic_baseline_favorite_border_24,
                R.string.title_likes,
                ResourcesCompat.getColor(resources,R.color.tabSelectedItem,null)
            )
                .build()
        )
        navView.onItemSelectedListener = { view: View, menuItem: MenuItem, b: Boolean ->
            when (menuItem.id) {
                R.id.navigation_wallpapers -> {
                    mainBinding.mainViewPager.currentItem=0
                }
                R.id.navigation_category -> {
                    mainBinding.mainViewPager.currentItem=1
                }
                R.id.navigation_likes -> {
                    mainBinding.mainViewPager.currentItem=2
                }
            }
        }

        val appVpAdapter = AppVpAdapter(supportFragmentManager, lifecycle, getFragments())
        mainBinding.mainViewPager.offscreenPageLimit=3
        mainBinding.mainViewPager.setPageTransformer(ZoomOutPageTransformer())
        mainBinding.mainViewPager.adapter = appVpAdapter
        mainBinding.mainViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> {
                        navView.menu.select(R.id.navigation_wallpapers)
                    }
                    1 -> {
                        navView.menu.select(R.id.navigation_category)

                    }
                    2 -> {
                        navView.menu.select(R.id.navigation_likes)

                    }
                }

            }
        })

    }

//    private fun getNavCells(): ArrayList<MeowBottomNavigationCell> {
//
//
//    }

    private fun getFragments(): ArrayList<Fragment> {
        val fragments = mutableListOf<Fragment>()
        fragments.add(WallpapersFrag())
        fragments.add(CategoryFrag())
        fragments.add(LikesFrag())
        return fragments as ArrayList<Fragment>
    }


    private class AppVpAdapter(
        fragmentManager: FragmentManager,
        lifecycle: Lifecycle,
        val fragments: ArrayList<Fragment>
    ) :
        FragmentStateAdapter(fragmentManager, lifecycle) {


        override fun getItemCount(): Int {
            return fragments.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragments.get(position)
        }

    }

}