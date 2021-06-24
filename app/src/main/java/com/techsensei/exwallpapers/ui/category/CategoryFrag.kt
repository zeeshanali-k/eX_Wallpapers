package com.techsensei.exwallpapers.ui.category

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.techsensei.exwallpapers.R
import com.techsensei.exwallpapers.ui.categoryDetail.CategoryDetailActivity
import com.techsensei.exwallpapers.utils.Constants
import com.techsensei.exwallpapers.utils.onRvClickListener

class CategoryFrag : Fragment(), onRvClickListener {
    private var inputMethodManager: InputMethodManager? = null
    private lateinit var categoriesList: ArrayList<String>
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_category, container, false)
    }

    private val TAG = "CategoryFrag"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.categories_frag_rv)
        //getting categories  array
        val categories = requireActivity().resources.getStringArray(R.array.anime_categories_names)
        categoriesList = ArrayList()
        categoriesList.addAll(categories)
        //setting up recycler view
        val rvAdapter = CategoryRvAdapter(categoriesList, this)
        recyclerView.layoutManager = LinearLayoutManager(
            requireActivity().applicationContext,
            RecyclerView.VERTICAL, false
        )
        recyclerView.adapter = rvAdapter

        val scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                hideKeyboard(recyclerView)
            }
        }
        recyclerView.addOnScrollListener(scrollListener)

//        setting up search
        val editText = view.findViewById<EditText>(R.id.category_search)
        editText.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d(TAG, "beforeTextChanged: $s")
            }

            override fun onTextChanged(
                newText: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                (recyclerView.adapter as CategoryRvAdapter).filter
                    .filter(newText)
            }

            override fun afterTextChanged(s: Editable?) {
                Log.d(TAG, "afterTextChanged: $s")
            }
        })

        editText.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                hideKeyboard(v)
            }
        }

    }

    private fun hideKeyboard(v: View) {
        if (inputMethodManager == null)
            inputMethodManager = ContextCompat.getSystemService(
                requireContext(),
                InputMethodManager::class.java
            )!!
        inputMethodManager!!.hideSoftInputFromWindow(v.windowToken, 0)
    }

    override fun onItemClicked(pos: Int) {
        val previewIntent = Intent(requireContext(), CategoryDetailActivity::class.java)
        previewIntent.putExtra(Constants.CATEGORY_KEY, categoriesList[pos])
        startActivity(previewIntent)
    }

    override fun onFavAdded(pos: Int) {

    }

    override fun onRvDownloadBtnClicked(pos: Int) {

    }

    private fun convertDpToPx(dp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            this.resources.displayMetrics
        )
    }

    private fun isKeyboardOpen(): Boolean {
        val visibleBounds = Rect()
        this.requireView().getWindowVisibleDisplayFrame(visibleBounds)
        val heightDiff = requireView().height - visibleBounds.height()
        val marginOfError = Math.round(convertDpToPx(50F))
        return heightDiff > marginOfError
    }

    private fun isKeyboardClosed(): Boolean {
        return !this.isKeyboardOpen()
    }
}