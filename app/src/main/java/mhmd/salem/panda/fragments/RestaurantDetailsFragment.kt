package mhmd.salem.panda.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import mhmd.salem.panda.Models.RestaurantAllMealModel
import mhmd.salem.panda.R
import mhmd.salem.panda.adapters.RestaurantAllMealAdapter
import mhmd.salem.panda.databinding.FragmentRestaurantDetailsBinding


class RestaurantDetailsFragment  : Fragment() {

    private lateinit var binding :FragmentRestaurantDetailsBinding
    private lateinit var uid     :String
    private lateinit var name    :String
    private lateinit var rating  :String
    private lateinit var imgLogo :String
    private lateinit var imgBackground :String
    private lateinit var location      :String
    private lateinit var description   :String
    private var firestore :FirebaseFirestore ? = null
    private lateinit var restaurantAllMealAdapter: RestaurantAllMealAdapter




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firestore = FirebaseFirestore.getInstance()
        restaurantAllMealAdapter = RestaurantAllMealAdapter()




    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRestaurantDetailsBinding.inflate(inflater , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            getDataByBundle()
            getRestaurantsAllMeals()



    }

    private fun getRestaurantsAllMeals() {
       firestore!!.collection("AllRestaurants").document(uid)
           .collection("AllMeals").addSnapshotListener{ values , error ->
               val resMealList = arrayListOf<RestaurantAllMealModel>()
               val resMealModel = values?.toObjects(RestaurantAllMealModel::class.java)
                   resMealList.addAll(resMealModel!!)

                   restaurantAllMealAdapter.differ.submitList(resMealList)

                    binding.recRes.apply {
                        layoutManager = LinearLayoutManager(context)
                        adapter       = restaurantAllMealAdapter
                    }


           }
    }

    private fun getDataByBundle() {
        val data = arguments
        uid           = data?.getString("uid").toString()
        name          = data?.getString("name").toString()
        rating        = data?.getString("rating").toString()
        imgLogo       = data?.getString("imgLogo").toString()
        location      = data?.getString("location").toString()
        description   = data?.getString("description").toString()
        imgBackground = data?.getString("imgBackground").toString()


        Glide.with(context!!)
            .load(imgLogo)
            .into(binding.imgRestuantDetails)

        Glide.with(context!!)
            .load(imgBackground)
            .into(binding.imgBackground)

        binding.nameResturant.text = name
        binding.ratingRes.text = rating
        binding.location.text  = location
        binding.resturantDescriptionDetials.text = description


    }

}