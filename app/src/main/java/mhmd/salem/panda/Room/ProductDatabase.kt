package mhmd.salem.panda.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import mhmd.salem.panda.Models.MartCatgeoryDetailsModel

@Database(entities = [MartCatgeoryDetailsModel::class] , version = 1)
@TypeConverters(ProductTypeConverters::class)
abstract class ProductDatabase :RoomDatabase(){
    abstract fun productDao () :ProductDao

    companion object
    {
            @Volatile
            var INSTANCE :ProductDatabase ? = null

           @Synchronized
        fun getInstance(context: Context):ProductDatabase
        {
                if (INSTANCE == null)
                {
                    INSTANCE = Room.databaseBuilder(
                        context,
                        ProductDatabase::class.java,
                        "producdDb",

                        ).fallbackToDestructiveMigration()
                        .build()
                }
            return INSTANCE as ProductDatabase

        }

    }

}