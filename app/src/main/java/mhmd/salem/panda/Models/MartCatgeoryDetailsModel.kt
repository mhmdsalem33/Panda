package mhmd.salem.panda.Models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "martProduct")
data class MartCatgeoryDetailsModel (
    @PrimaryKey
    var id          :String = "",
    var name        :String = "",
    var imgUrl      :String = "",
    var price       :Int = 0 ,
    var description :String = ""


        )