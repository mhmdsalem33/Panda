package mhmd.salem.panda.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import at.favre.lib.crypto.bcrypt.BCrypt
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import mhmd.salem.panda.adapters.MartAdapter
import mhmd.salem.panda.Models.MartModel
import mhmd.salem.panda.Models.UserModel
import mhmd.salem.panda.R
import mhmd.salem.panda.databinding.FragmentHomeBinding
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment() {

    private lateinit var binding :FragmentHomeBinding
    private var firestore    :FirebaseFirestore ? = null
    private var firebaseAuth :FirebaseAuth      ? = null
    private lateinit var martAdapter: MartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        firestore    = FirebaseFirestore.getInstance()
        martAdapter  = MartAdapter()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        getAllMartsData()
        onFoodDeliveryClick()
        onMartClick()
        onImageProfileClick()
        getPersonalInformation()
        setWelcomeMessage()
        onSearchBoxClick()







    }

    private fun onSearchBoxClick() {
       binding.searchBox.setOnClickListener {
           findNavController().navigate(R.id.searchFragment)
       }
    }

    private fun setWelcomeMessage() {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        if (hour in 1..12)
        {
            binding.welcome.text = "Good morning"
        }
        else if (hour in 12..17)
        {
            binding.welcome.text = "Good afternoon"
        }
        else
        {
            binding.welcome.text = "Good evening"
        }
    }

    private fun getPersonalInformation() {
        FirebaseDatabase.getInstance().getReference("Users")
            .child(firebaseAuth!!.currentUser!!.uid)
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userModel = snapshot.getValue(UserModel::class.java)
                    Glide.with(context!!)
                        .load(userModel!!.avatar)
                        .into(binding.imgProfile)
                    binding.name.text = userModel.name
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, ""+error.message, Toast.LENGTH_SHORT).show()
                }

            })
    }
    private fun onImageProfileClick() {
        binding.imgProfile.setOnClickListener {
            findNavController().navigate(R.id.profileFragment)
        }
    }
    private fun onMartClick() {
        martAdapter.onMartClick = { data ->
            val fragment = MartFragment()
            val bundle = Bundle()
                bundle.putString("uid"           , data.id)
                bundle.putString("name"          , data.name)
                bundle.putString("time"          , data.time)
                bundle.putString("delivery_fee"  , data.deliveryFee)
                bundle.putString("min_amount"    , data.minAmount)
              fragment.arguments = bundle
            findNavController().navigate(R.id.martFragment , bundle)
        }
    }
    private fun onFoodDeliveryClick() {
        binding.popImage.setOnClickListener {
            findNavController().navigate(R.id.restaurantsFragment)
        }
    }
    private fun getAllMartsData() {
        firestore!!.collection("Mart").addSnapshotListener{ values , errors ->
            val martList  = arrayListOf<MartModel>()
            val martModel = values?.toObjects(MartModel::class.java)
                martList.addAll(martModel!!)

            martAdapter.setMart(martList =  martList as ArrayList<MartModel>)
            binding.recMartHome.apply {
                layoutManager = LinearLayoutManager(context , RecyclerView.HORIZONTAL , false)
                adapter       = martAdapter //MartAdapter(martList)

            }

        }
    }

}