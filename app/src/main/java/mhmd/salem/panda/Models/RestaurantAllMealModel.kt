package mhmd.salem.panda.Models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "mealInformation")
data class RestaurantAllMealModel (
    @PrimaryKey
    var id      :String = "",
    var name    :String = "",
    var price   :Int    = 0,
    var imgUrl  :String = "" ,
    var description :String=""

        )
