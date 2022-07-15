package mhmd.salem.panda.Room

import androidx.room.TypeConverter
import androidx.room.TypeConverters

@TypeConverters
class ProductTypeConverters {

    @TypeConverter
    fun fromAnyToString(attribute :Any?) :String{
            if (attribute == null)
                return ""
        return attribute as String
    }

    fun fromStringToAny(attribute :String?):Any{
        if (attribute == null)
            return ""
        return attribute
    }

}