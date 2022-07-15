package mhmd.salem.panda.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import mhmd.salem.panda.Room.MealDatabase

class MealViewModelFactory(
    val mealDatabase: MealDatabase
):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MealViewModel(mealDatabase) as T
    }
}