package mhmd.salem.panda.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.protobuf.Value
import mhmd.salem.panda.Models.CartModel
import mhmd.salem.panda.Models.UserModel
import mhmd.salem.panda.R
import mhmd.salem.panda.databinding.ActivityPlaceOrderBinding

class PlaceOrderActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPlaceOrderBinding
    private var firestore    :FirebaseFirestore ? = null
    private var firebaseAuth :FirebaseAuth ? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaceOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth  = FirebaseAuth.getInstance()
        firestore     = FirebaseFirestore.getInstance()


        val list :List<CartModel>? =
                intent.getSerializableExtra("itemList") as ArrayList<CartModel>?


        if (list !=  null && list.size > 0)
        {
        for (model in list)
        {
                val cartMap =HashMap<String , Any>()
                    cartMap["id"]              = model.id
                    cartMap["name"]            = model.name
                    cartMap["imgUrl"]          = model.imgUrl
                    cartMap["price"]           = model.price
                    cartMap["totalPrice"]      = model.totalPrice
                    cartMap["totalQuantity"]   = model.totalQuantity
                    cartMap["currentDate"]     = model.currentDate
                    cartMap["currentTime"]     = model.currentTime

                firestore!!.collection("CurrentUser")
                    .document(firebaseAuth!!.currentUser!!.uid)
                    .collection("MyOrder")
                    .add(cartMap)
                    .addOnCompleteListener{ it->
                        it.addOnSuccessListener {
                            Toast.makeText(applicationContext, "We received your Order ", Toast.LENGTH_SHORT).show()
                        }
                        it.addOnFailureListener{
                            Toast.makeText(applicationContext, ""+it.message, Toast.LENGTH_SHORT).show()
                        }
                    }

            FirebaseDatabase.getInstance().getReference("Users")
                .child(firebaseAuth!!.currentUser!!.uid)
                .addValueEventListener(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val userModel = snapshot.getValue(UserModel::class.java)


                        val userMap = HashMap<String , Any>()

                            userMap["uid"]         = userModel!!.id
                            userMap["name"]        = userModel.name
                            userMap["email"]       = userModel.email
                            userMap["avatar"]      = userModel.avatar
                            userMap["address"]     = userModel.address
                            userMap["phoneNumber"] = userModel.phoneNumber


                        firestore!!.collection("AllOrders")
                            .document(userModel.id)
                            .set(userMap)

                            firestore!!.collection("AllOrders")
                                .document(userModel.id)
                                .collection("orders")
                                .add(cartMap)





                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(applicationContext, ""+error.message, Toast.LENGTH_SHORT).show()
                    }

                })




        }
        }


    }
}