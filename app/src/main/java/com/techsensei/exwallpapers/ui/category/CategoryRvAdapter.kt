package com.techsensei.exwallpapers.ui.category

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.techsensei.exwallpapers.databinding.CategoryRvLayoutBinding
import com.techsensei.exwallpapers.utils.onRvClickListener
import java.util.*
import kotlin.collections.ArrayList

class CategoryRvAdapter(private val categories: ArrayList<String>,private val listener:onRvClickListener): RecyclerView.Adapter<CategoryRvAdapter.CategoryHolder>(),Filterable {

    private var categoriesFull=ArrayList<String>()

    init {
        categoriesFull.addAll(categories)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        return CategoryHolder(CategoryRvLayoutBinding
                .inflate(LayoutInflater.from(parent.context),
                        parent,false))
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        holder.rvBinding.root.setOnClickListener {
            listener.onItemClicked(position)
        }
        holder.rvBinding.category= categories[position]
        holder.rvBinding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    class CategoryHolder(val rvBinding: CategoryRvLayoutBinding) : RecyclerView.ViewHolder(rvBinding.root)

    override fun getFilter(): Filter {
        return filter
    }

    private val filter:Filter=object : Filter(){
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val categoriesTemp=ArrayList<String>()
            categoriesTemp.clear()
            if (constraint==null || constraint.isEmpty()){
                categoriesTemp.addAll(categoriesFull)
            }else{
                for (category:String in categoriesFull) {
                    if (category.toLowerCase(Locale.ROOT).contains(constraint.toString().toLowerCase(Locale.ROOT)))
                        categoriesTemp.add(category)
                }
            }

            val filterResults=FilterResults()
            filterResults.values=categoriesTemp
            return filterResults
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            categories.clear()
            categories.addAll(results!!.values as ArrayList<String>)
            notifyDataSetChanged()
        }
    }

}
