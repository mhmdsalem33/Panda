package mhmd.salem.panda.Room

import androidx.lifecycle.LiveData
import androidx.room.*
import mhmd.salem.panda.Models.MartCatgeoryDetailsModel

@Dao
interface ProductDao {

    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    suspend fun upsert(martModel : MartCatgeoryDetailsModel)

    @Delete
    suspend fun delete(martModel  : MartCatgeoryDetailsModel)

    @Query("SELECT * FROM martProduct")
    fun getAllProducts() :LiveData<List<MartCatgeoryDetailsModel>>



}