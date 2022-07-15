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
import mhmd.salem.panda.Models.RestaurantAllMealModel
import mhmd.salem.panda.ViewModels.MealViewModel
import mhmd.salem.panda.activites.MainActivity
import mhmd.salem.panda.databinding.RestaurantRowBinding
import mhmd.salem.panda.databinding.ResturantDetailsRowBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class RestaurantAllMealAdapter():RecyclerView.Adapter<RestaurantAllMealAdapter.ViewHolder>() {



    private var firestore    :FirebaseFirestore ? = null
    private var firebaseAuth :FirebaseAuth ? = null

    private lateinit var mealMvvm : MealViewModel

    val diffUtil = object  :DiffUtil.ItemCallback<RestaurantAllMealModel>()
    {
        override fun areItemsTheSame(oldItem: RestaurantAllMealModel, newItem: RestaurantAllMealModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RestaurantAllMealModel, newItem: RestaurantAllMealModel): Boolean {
            return  oldItem == newItem
        }

    }
    var differ = AsyncListDiffer(this , diffUtil)

    class ViewHolder(val binding : ResturantDetailsRowBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
     return ViewHolder(ResturantDetailsRowBinding.inflate(LayoutInflater.from(parent.context) , parent ,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        firestore    = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        mealMvvm = (holder.itemView.context  as MainActivity).mealMvvm

        val data = differ.currentList[position]
       Glide.with(holder.itemView)
           .load(data.imgUrl)
           .into(holder.binding.imgRow)

        holder.binding.mealName.text         = data.name
        holder.binding.rowDescription.text   = data.description
        holder.binding.txtPrice.text         = "$"+data.price.toString()


         holder.binding.fav.setOnClickListener {
            data.let {
                mealMvvm.insertMeal(it)
                Toast.makeText(holder.itemView.context, "Meal Saved", Toast.LENGTH_SHORT).show()
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

            val currentDate     = SimpleDateFormat("MM , dd : yyyy")
            val saveCurrentDate = currentDate.format(Calendar.getInstance().time)

            val currentTime     = SimpleDateFormat("HH:mm:ss a")
            val saveCurrentTime = currentTime.format(Calendar.getInstance().time)

            val uniqueId = firestore!!.collection("CurrentUser").document().id
            val cartMap = HashMap<String , Any>()
                cartMap["id"]              = uniqueId
                cartMap["name"]            = data.name
                cartMap["price"]           = data.price
                cartMap["totalPrice"]      = totalQuantity * data.price
                cartMap["totalQuantity"]   = totalQuantity
                cartMap["currentDate"]     = saveCurrentDate
                cartMap["currentTime"]     = saveCurrentTime
                cartMap["imgUrl"]          = data.imgUrl

            firestore!!.collection("CurrentUser")
                .document(firebaseAuth!!.currentUser!!.uid)
                .collection("AddToCart")
                .document(uniqueId)
                .set(cartMap)
                .addOnCompleteListener{ it ->
                    it.addOnSuccessListener {
                        Toast.makeText(holder.itemView.context, "Item Added success to cart", Toast.LENGTH_SHORT).show()
                    }
                    it.addOnFailureListener{
                        Toast.makeText(holder.itemView.context, ""+it.message, Toast.LENGTH_SHORT).show()
                    }
                }

        }

    }

    override fun getItemCount() = differ.currentList.size

}