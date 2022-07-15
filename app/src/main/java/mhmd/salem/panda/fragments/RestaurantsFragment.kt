package mhmd.salem.panda.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import mhmd.salem.panda.Models.AllRestaurantsModel
import mhmd.salem.panda.R
import mhmd.salem.panda.adapters.AllRestaurantsAdapter
import mhmd.salem.panda.databinding.FragmentRestaurantsBinding

class RestaurantsFragment : Fragment() {

    private lateinit var binding :FragmentRestaurantsBinding
    private var firestore        :FirebaseFirestore ? = null
    private lateinit var allRestaurantsAdapter: AllRestaurantsAdapter




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firestore             = FirebaseFirestore.getInstance()
        allRestaurantsAdapter = AllRestaurantsAdapter()



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRestaurantsBinding.inflate(inflater , container , false)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        getAllRestaurantsDataFromFirestore()
        onItemRestaurantClick()
        onCustomToolbarClick()




    }

    private fun onCustomToolbarClick() {
        binding.included.arrowBack.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }
        binding.included.iconFav.setOnClickListener {
            findNavController().navigate(R.id.favoriteFragment)
        }
        binding.included.iconShop.setOnClickListener {
            findNavController().navigate(R.id.cartFragment)
        }
    }

    private fun onItemRestaurantClick() {
        allRestaurantsAdapter.onItemRestaurantClick = { data ->

            val fragment = RestaurantDetailsFragment()
            val bundle   = Bundle()
                bundle.putString("uid"            , data.id)
                bundle.putString("name"           , data.name)
                bundle.putString("rating"         , data.rating)
                bundle.putString("imgLogo"        , data.img_logo)
                bundle.putString("location"       , data.location)
                bundle.putString("description"    , data.description)
                bundle.putString("imgBackground"  , data.img_background)
            fragment.arguments = bundle


            findNavController().navigate(R.id.restaurantDetailsFragment , bundle)
        }
    }

    private fun getAllRestaurantsDataFromFirestore() {

        firestore!!.collection("AllRestaurants").addSnapshotListener{values , error ->
            val allRestaurantsList   = arrayListOf<AllRestaurantsModel>()
            val allRestaurantsModel  = values?.toObjects(AllRestaurantsModel::class.java)
            allRestaurantsList.addAll(allRestaurantsModel!!)

            allRestaurantsAdapter.differ.submitList(allRestaurantsList)

            binding.recRestaurants.apply {
                layoutManager = LinearLayoutManager(context , RecyclerView.VERTICAL, false)
                adapter       = allRestaurantsAdapter
            }

            binding.countOfRest.text = allRestaurantsList.size.toString()

        }

    }
}





