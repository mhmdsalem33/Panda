package mhmd.salem.panda.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import mhmd.salem.panda.Models.MartModel
import mhmd.salem.panda.databinding.MartRowBinding

class MartAdapter():RecyclerView.Adapter<MartAdapter.ViewHolder>() {

    lateinit var onMartClick :((MartModel) -> Unit)

    var martList  = ArrayList<MartModel>()
    fun setMart(martList :ArrayList<MartModel>)
    {
        this.martList =  martList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding : MartRowBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(MartRowBinding.inflate(LayoutInflater.from(parent.context) ,parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

       Glide.with(holder.itemView)
           .load(martList[position].imgUrl)
           .into(holder.binding.imgPandaMart)

    holder.itemView.setOnClickListener {
        onMartClick.invoke(martList[position])
    }

    }

    override fun getItemCount() = martList.size


}