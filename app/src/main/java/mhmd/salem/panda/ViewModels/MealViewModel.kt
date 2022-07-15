package mhmd.salem.panda.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mhmd.salem.panda.Models.RestaurantAllMealModel
import mhmd.salem.panda.Room.MealDatabase

class MealViewModel(
    val mealDatabase: MealDatabase
):ViewModel() {

    val favoriteMealLiveData = mealDatabase.mealDao().getALLMeals()


    fun observerFavoriteMealLiveData() :LiveData<List<RestaurantAllMealModel>>
    {
        return favoriteMealLiveData
    }


    fun insertMeal(meal :RestaurantAllMealModel)
    {
        viewModelScope.launch {
            mealDatabase.mealDao().upsert(meal)
        }
    }

    fun deleteMeal(meal :RestaurantAllMealModel)
    {
        viewModelScope.launch {
            mealDatabase.mealDao().delete(meal)
        }
    }

    var martUid :String ? = null

}