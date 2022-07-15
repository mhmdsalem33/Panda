package mhmd.salem.panda.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import mhmd.salem.panda.Models.MartCategoriesModel
import mhmd.salem.panda.databinding.CategoryMartRowBinding

class SearchMartCategoriesAdapter(var searchList :List<MartCategoriesModel>):RecyclerView.Adapter<SearchMartCategoriesAdapter.ViewHolder>() {


    lateinit var onItemClick :((MartCategoriesModel) -> Unit)

    class ViewHolder(val binding :CategoryMartRowBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       return ViewHolder(CategoryMartRowBinding.inflate(LayoutInflater.from(parent.context) , parent , false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       Glide.with(holder.itemView)
           .load(searchList[position].imgUrl)
           .into(holder.binding.imageMar)
        holder.binding.prodName.text = searchList[position].name


        holder.itemView.setOnClickListener {
            onItemClick.invoke(searchList[position])
        }
    }

    override fun getItemCount() = searchList.size

}