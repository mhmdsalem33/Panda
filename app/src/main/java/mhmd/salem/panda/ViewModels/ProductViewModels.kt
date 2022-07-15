package mhmd.salem.panda.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mhmd.salem.panda.Models.MartCatgeoryDetailsModel
import mhmd.salem.panda.Room.ProductDatabase

class ProductViewModels(
    val productDatabase : ProductDatabase
):ViewModel() {


    private var favoriteProductLiveData = productDatabase.productDao().getAllProducts()

    fun observerFavoriteMealLiveData() :LiveData<List<MartCatgeoryDetailsModel>>{
        return favoriteProductLiveData
    }

    fun insertProduct(martProduct :MartCatgeoryDetailsModel)
    {
            viewModelScope.launch {
                    productDatabase.productDao().upsert(martProduct)
            }
    }

    fun deleteProduct(martProduct: MartCatgeoryDetailsModel){
        viewModelScope.launch {
            productDatabase.productDao().delete(martProduct)
        }
    }


}