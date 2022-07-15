package mhmd.salem.panda.Room

import androidx.lifecycle.LiveData
import androidx.room.*
import mhmd.salem.panda.Models.RestaurantAllMealModel

@Dao
interface MealDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(meal : RestaurantAllMealModel)

    @Delete
    suspend fun delete(meal: RestaurantAllMealModel)

    @Query("SELECT * FROM mealInformation")
    fun getALLMeals() :LiveData<List<RestaurantAllMealModel>>

}