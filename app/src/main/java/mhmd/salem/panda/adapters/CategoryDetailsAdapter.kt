package mhmd.salem.panda.adapters

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
import mhmd.salem.panda.Models.MartCatgeoryDetailsModel
import mhmd.salem.panda.ViewModels.ProductViewModels
import mhmd.salem.panda.activites.MainActivity
import mhmd.salem.panda.databinding.ResturantDetailsRowBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class CategoryDetailsAdapter():RecyclerView.Adapter<CategoryDetailsAdapter.ViewHolder>() {



    private var firebaseAuth :FirebaseAuth      ? = null
    private var firestore    :FirebaseFirestore ? = null

    private lateinit var productMvvm : ProductViewModels

    val diffUtil = object  :DiffUtil.ItemCallback<MartCatgeoryDetailsModel>(){
        override fun areItemsTheSame(oldItem: MartCatgeoryDetailsModel, newItem: MartCatgeoryDetailsModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MartCatgeoryDetailsModel, newItem: MartCatgeoryDetailsModel): Boolean {
           return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this , diffUtil)

    class ViewHolder(val binding :ResturantDetailsRowBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       return ViewHolder(ResturantDetailsRowBinding.inflate(LayoutInflater.from(parent.context) ,parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        firebaseAuth = FirebaseAuth.getInstance()
        firestore    = FirebaseFirestore.getInstance()

        productMvvm = (holder.itemView.context as MainActivity).productMvvm

        val data = differ.currentList[position]

        Glide.with(holder.itemView)
            .load(data.imgUrl)
            .into(holder.binding.imgRow)

        holder.binding.mealName.text        = data.name
        holder.binding.txtPrice.text        = "$"+data.price.toString()
        holder.binding.rowDescription.text  = data.description



        holder.binding.fav.setOnClickListener {
            data.let {
                productMvvm.insertProduct(it)
                Toast.makeText(holder.itemView.context, "Product Saved", Toast.LENGTH_SHORT).show()
            }
        }

        var totalQuantity = 1
        holder.binding.add.setOnClickListener {
            if (totalQuantity < 10)
            {
               totalQuantity++
               holder.binding.quntity.text = totalQuantity.toString()
            }
        }
        holder.binding.remove.setOnClickListener {
            if (totalQuantity > 1)
            {
                totalQuantity--
                holder.binding.quntity.text = totalQuantity.toString()
            }
        }

        holder.binding.addToBasket.setOnClickListener {

            val currentDate     = SimpleDateFormat("MM dd , yyyy")
            val saveCurrentDate = currentDate.format(Calendar.getInstance().time)

            val currentTime     = SimpleDateFormat("HH:mm:ss a")
            val saveCurrentTime = currentTime.format(Calendar.getInstance().time)

            val uniqueId = firestore!!.collection("CurrentUser").document().id
            val finalData = CartModel(
                id            =  uniqueId      ,
                name          =  data.name     ,
                price         =  data.price    ,
                imgUrl        =  data.imgUrl   ,
                totalPrice    =  totalQuantity * data.price ,
                totalQuantity =  totalQuantity  ,
                currentDate   =  saveCurrentDate,
                currentTime   =  saveCurrentTime,


            )
            /*

            val cartMap  = HashMap<String , Any>()
                cartMap["id"]     =  uniqueId
                cartMap["name"]   =  data.name
                cartMap["price"]  =  data.price
                cartMap["imgUrl"] =  data.imgUrl
                cartMap["totalQuantity"] = totalQuantity
                cartMap["totalPrice"]    = totalQuantity * data.price
                cartMap["currentTime"]   = saveCurrentTime
                cartMap["currentDate"]   = saveCurrentDate


             */
            firestore!!.collection("CurrentUser")
                .document(firebaseAuth!!.currentUser!!.uid)
                .collection("AddToCart")
                .document(uniqueId)
                .set(finalData)
                .addOnCompleteListener{ it ->
                    it.addOnSuccessListener {
                        Toast.makeText(holder.itemView.context, "${data.name} added success", Toast.LENGTH_SHORT).show()
                    }
                    it.addOnFailureListener{
                        Toast.makeText(holder.itemView.context, ""+it.message, Toast.LENGTH_SHORT).show()
                    }
                }




        }



    }

    override fun getItemCount() = differ.currentList.size

}