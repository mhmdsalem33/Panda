package mhmd.salem.panda.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import mhmd.salem.panda.Room.ProductDatabase

class ProductViewModelFactory(
    val productDatabase : ProductDatabase
):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
       return  ProductViewModels(productDatabase) as T
    }
}