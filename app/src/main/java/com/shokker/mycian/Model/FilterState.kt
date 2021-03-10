package com.shokker.mycian.Model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter


// состояние текущего фильтра запросов
@Entity(tableName = "filterState")
data class FilterState(
                        val fromPrice: Int,
                        val toPrice: Int,
                        val fromSq: Int,
                        val toSq: Int,
                        val fType: FilterType,
                        @PrimaryKey var id: Int = 1
)
{
    enum class FilterType{
        Sale, Rent
    }
    class Converter{
        @TypeConverter
        fun toFilterType(value: Int) = enumValues<FilterType>()[value]
        @TypeConverter
        fun fromFilterType(value: FilterType) =  value.ordinal
    }
}


