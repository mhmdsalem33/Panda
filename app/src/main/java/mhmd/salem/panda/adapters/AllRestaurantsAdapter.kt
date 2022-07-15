package mhmd.salem.panda.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import mhmd.salem.panda.Models.AllRestaurantsModel
import mhmd.salem.panda.databinding.RestaurantRowBinding


class AllRestaurantsAdapter():RecyclerView.Adapter<AllRestaurantsAdapter.ViewHolder>() {


        private var firestore : FirebaseFirestore ? = null
        lateinit var onItemRestaurantClick :((AllRestaurantsModel) -> Unit)


        val diffUtil = object :DiffUtil.ItemCallback<AllRestaurantsModel>(){
            override fun areItemsTheSame(oldItem: AllRestaurantsModel, newItem: AllRestaurantsModel): Boolean {
                return oldItem.id == newItem.id

            }

            override fun areContentsTheSame(oldItem: AllRestaurantsModel, newItem: AllRestaurantsModel): Boolean {
                return oldItem == newItem
            }

        }
        val differ =  AsyncListDiffer(this , diffUtil)

    class ViewHolder(val binding : RestaurantRowBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      return ViewHolder(RestaurantRowBinding.inflate(LayoutInflater.from(parent.context) ,parent , false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        firestore = FirebaseFirestore.getInstance()

        val data = differ.currentList[position]
       Glide.with(holder.itemView)
           .load(data.img_logo)
           .into(holder.binding.imgRestuant)

        Glide.with(holder.itemView)
            .load(data.base_img)
            .into(holder.binding.imgCategory)

        holder.binding.nameResturant.text        = data.name
        holder.binding.resturantDescription.text = data.description
        holder.binding.rating.text               = data.rating
        holder.binding.timeRes.text              = data.time
        holder.binding.deliveryFee.text          = "$"+data.delivery_fee

        holder.itemView.setOnClickListener {
            onItemRestaurantClick.invoke(data)
        }







    }

    override fun getItemCount() = differ.currentList.size

}