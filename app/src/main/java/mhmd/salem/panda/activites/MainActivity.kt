package mhmd.salem.panda.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import mhmd.salem.panda.R
import mhmd.salem.panda.Room.MealDatabase
import mhmd.salem.panda.Room.ProductDatabase
import mhmd.salem.panda.ViewModels.MealViewModel
import mhmd.salem.panda.ViewModels.MealViewModelFactory
import mhmd.salem.panda.ViewModels.ProductViewModelFactory
import mhmd.salem.panda.ViewModels.ProductViewModels
import mhmd.salem.panda.databinding.ActivityMainBinding
import mhmd.salem.panda.fragments.CartFragment
import mhmd.salem.panda.fragments.FavoriteFragment
import mhmd.salem.panda.fragments.HomeFragment
import mhmd.salem.panda.fragments.RestaurantsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding


    val productMvvm : ProductViewModels by lazy {
        val productDatabase = ProductDatabase.getInstance(this)
        val productViewModelFactory = ProductViewModelFactory(productDatabase)
        ViewModelProvider(this , productViewModelFactory)[ProductViewModels::class.java]
    }

    val mealMvvm : MealViewModel by lazy {
        val mealDatabase = MealDatabase.getInstance(this)
        val mealViewModelFactory = MealViewModelFactory(mealDatabase)
        ViewModelProvider(this , mealViewModelFactory)[MealViewModel::class.java]
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


            /*
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
       // val findNav   = Navigation.findNavController(this , R.id.host_fragment)
      //  NavigationUI.setupWithNavController(bottomNav, findNav)

        bottomNav.setOnItemSelectedListener {

            when(it.itemId)
            {
                R.id.homeFragment ->
                {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.host_fragment , HomeFragment())
                        .addToBackStack(null)
                        .commit()
                }

                R.id.restaurantsFragment ->
                {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.host_fragment , RestaurantsFragment())
                        .addToBackStack(null)
                        .commit()
                }


                R.id.favoriteFragment ->
                {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.host_fragment , FavoriteFragment())
                        .addToBackStack(null)
                        .commit()
                }


                R.id.cartFragment ->
                {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.host_fragment , CartFragment())
                        .addToBackStack(null)
                        .commit()
                }




            }
            true
        }


             */

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.setOnItemSelectedListener {
            when (it.itemId){
                R.id.homeFragment ->
                {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.host_fragment, HomeFragment())
                        .addToBackStack(null)
                        .commit()
                }
                R.id.restaurantsFragment ->
                {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.host_fragment, RestaurantsFragment())
                       .addToBackStack(null)
                        .commit()
                }
                R.id.favoriteFragment ->
                {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.host_fragment, FavoriteFragment())
                        .addToBackStack(null)
                        .commit()
                }
                R.id.cartFragment ->
                {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.host_fragment, CartFragment())
                        .addToBackStack(null)
                        .commit()
                }


            }

            true
        }

    }
}