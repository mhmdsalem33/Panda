package mhmd.salem.panda.Models

import java.io.Serializable

data class CartModel (
    var id            :String = "",
    var name          :String = "",
    var currentTime   :String = "",
    var currentDate   :String = "",
    var imgUrl        :String = "",
    var price         :Int    = 1,
    var totalPrice    :Int    = 1,
    var totalQuantity :Int    = 1

):Serializable