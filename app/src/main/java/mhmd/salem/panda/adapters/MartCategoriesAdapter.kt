package mhmd.salem.panda.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import mhmd.salem.panda.Models.MartCategoriesModel
import mhmd.salem.panda.ViewModels.MealViewModel
import mhmd.salem.panda.activites.MainActivity
import mhmd.salem.panda.databinding.CategoryMartRowBinding

class MartCategoriesAdapter():RecyclerView.Adapter<MartCategoriesAdapter.ViewHolder>() {


    private lateinit var mealMvvm :MealViewModel

    lateinit var onCategoryClick :((MartCategoriesModel) -> Unit)

    var diffUtil = object :DiffUtil.ItemCallback<MartCategoriesModel>(){
        override fun areItemsTheSame(oldItem: MartCategoriesModel, newItem: MartCategoriesModel): Boolean {
           return oldItem.id ==  newItem.id
        }

        override fun areContentsTheSame(oldItem: MartCategoriesModel, newItem: MartCategoriesModel
        ): Boolean {
          return  oldItem == newItem
        }
    }

    var differ = AsyncListDiffer(this , diffUtil )



    class ViewHolder(val binding:CategoryMartRowBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      return  ViewHolder(CategoryMartRowBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        mealMvvm = (holder.itemView.context as MainActivity).mealMvvm
        mealMvvm.martUid = differ.currentList[position].id

       Glide.with(holder.itemView)
           .load(differ.currentList[position].imgUrl)
           .into(holder.binding.imageMar)

        holder.binding.prodName.text = differ.currentList[position].name

        holder.itemView.setOnClickListener {
            onCategoryClick.invoke(differ.currentList[position])
        }



    }

    override fun getItemCount() = differ.currentList.size


}