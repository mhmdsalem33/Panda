package mhmd.salem.panda.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import mhmd.salem.panda.Models.SearchMartModel
import mhmd.salem.panda.databinding.MartRowBinding

class SearchMartAdapter(var searchList :List<SearchMartModel>):RecyclerView.Adapter<SearchMartAdapter.ViewHolder>() {



    lateinit var onItemClick :((SearchMartModel) -> Unit)

    class ViewHolder(val binding : MartRowBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      return  ViewHolder(MartRowBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       Glide.with(holder.itemView)
           .load(searchList[position].imgUrl)
           .into(holder.binding.imgPandaMart)


        holder.itemView.setOnClickListener {
                    onItemClick.invoke(searchList[position])
        }

    }

    override fun getItemCount() = searchList.size

}