package mhmd.salem.panda.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import mhmd.salem.panda.Models.ViewPagerModel
import mhmd.salem.panda.databinding.ViewPagerRowBinding

class ViewPagerAdapter( private val viewPager2: ViewPager2):RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {


    private var viewPagerList      = ArrayList<ViewPagerModel>()
    fun setViewPager(viewPagerList : ArrayList<ViewPagerModel>)
    {
        this.viewPagerList = viewPagerList
        notifyDataSetChanged()
    }



    class ViewHolder(val binding :ViewPagerRowBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       return ViewHolder(ViewPagerRowBinding.inflate(LayoutInflater.from(parent.context) , parent , false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(viewPagerList[position].imgUrl)
            .into(holder.binding.imageView)

        if (position == viewPagerList.size-1)
        {
            viewPager2.post(runnable)
        }

    }

    override fun getItemCount() = viewPagerList.size


    private val runnable = Runnable {
        viewPagerList.addAll(viewPagerList)
        notifyDataSetChanged()
    }




}