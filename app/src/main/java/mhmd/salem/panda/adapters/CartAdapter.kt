package mhmd.salem.panda.adapters

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import mhmd.salem.panda.Models.CartModel
import mhmd.salem.panda.databinding.MyCartRowBinding

class CartAdapter():RecyclerView.Adapter<CartAdapter.ViewHolder>() {


    private var firestore     :FirebaseFirestore ? = null
    private var firebaseAuth  :FirebaseAuth      ? = null
    lateinit var onDeleteClick :((CartModel) -> Unit)

    val diffUtil = object :DiffUtil.ItemCallback<CartModel>()
    {
        override fun areItemsTheSame(oldItem: CartModel, newItem: CartModel): Boolean {
            return  oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CartModel, newItem: CartModel): Boolean {
            return oldItem ==  newItem
        }
    }
    val differ = AsyncListDiffer(this , diffUtil)

    class ViewHolder (val binding :MyCartRowBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return  ViewHolder(MyCartRowBinding.inflate(LayoutInflater.from(parent.context) , parent , false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        firestore    = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        val data     = differ.currentList[position]

        Glide.with(holder.itemView)
            .load(data.imgUrl)
            .into(holder.binding.imgRow)

        holder.binding.producatName.text   = data.name
        holder.binding.time.text           = data.currentTime
        holder.binding.producatPrice.text  = "Price $${data.price}"
        holder.binding.totalPrice.text     = "Total Price   $${data.totalPrice}"
        holder.binding.totalQuantity.text  = "Total Quantity ${data.totalQuantity}"

        holder.binding.delete.setOnClickListener {
            onDeleteClick.invoke(data)
        }


    }

    override fun getItemCount() = differ.currentList.size

}