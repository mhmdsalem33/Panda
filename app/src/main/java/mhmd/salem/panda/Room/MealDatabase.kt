package mhmd.salem.panda.Room


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import mhmd.salem.panda.Models.RestaurantAllMealModel

@Database(entities = [RestaurantAllMealModel::class] , version = 1)
@TypeConverters(MealTypeConverters::class)
abstract class MealDatabase :RoomDatabase() {
    abstract fun mealDao() :MealDao


    companion object
    {
        @Volatile
        var INSTANCE :MealDatabase ? = null

        @Synchronized
        fun getInstance(context: Context):MealDatabase{
            if (INSTANCE == null)
            {
                INSTANCE = Room.databaseBuilder(
                    context,
                    MealDatabase::class.java,
                    "mealdb"
                ).fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE as MealDatabase
        }
    }
}