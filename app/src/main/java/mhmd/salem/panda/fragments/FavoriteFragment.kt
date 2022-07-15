package mhmd.salem.panda.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import mhmd.salem.panda.R
import mhmd.salem.panda.ViewModels.MealViewModel
import mhmd.salem.panda.ViewModels.ProductViewModels
import mhmd.salem.panda.activites.MainActivity
import mhmd.salem.panda.adapters.FavoriteMartCategoryAdapter
import mhmd.salem.panda.adapters.FavoriteMealRestaurantsAdapter
import mhmd.salem.panda.databinding.FragmentFavoriteBinding


class FavoriteFragment : Fragment() {

    private lateinit var binding :FragmentFavoriteBinding
    private lateinit var productMvvm :ProductViewModels
    private lateinit var mealMvvm : MealViewModel
    private lateinit var favoriteMartCategoryAdapter: FavoriteMartCategoryAdapter
    private lateinit var favoriteMealRestaurantsAdapter: FavoriteMealRestaurantsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productMvvm = (context as MainActivity).productMvvm
        mealMvvm    = (context as MainActivity).mealMvvm
        favoriteMartCategoryAdapter    = FavoriteMartCategoryAdapter()
        favoriteMealRestaurantsAdapter = FavoriteMealRestaurantsAdapter()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavoriteBinding.inflate(inflater , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        observerFavoriteProductsMart()
        observerFavoriteMealRestaurants()




    }


    private fun observerFavoriteMealRestaurants() {
        mealMvvm.observerFavoriteMealLiveData().observe(viewLifecycleOwner , Observer { data ->
           favoriteMealRestaurantsAdapter.differ.submitList(data)

        })
        binding.recFavoriteMeal.apply {
            layoutManager = LinearLayoutManager(context)
            adapter       = favoriteMealRestaurantsAdapter
        }
    }

    private fun observerFavoriteProductsMart() {
        productMvvm.observerFavoriteMealLiveData().observe(viewLifecycleOwner , Observer { data ->

                favoriteMartCategoryAdapter.differ.submitList(data)

        })
        binding.recFavorite.apply {
            layoutManager = LinearLayoutManager(context)
            adapter       = favoriteMartCategoryAdapter
        }
    }


}