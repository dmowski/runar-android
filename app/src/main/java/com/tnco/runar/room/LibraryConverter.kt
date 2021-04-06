package com.tnco.runar.room

import androidx.room.TypeConverter
import java.util.stream.Collectors

class LibraryConverter {

    @TypeConverter
    fun fromChilds(items: List<String>?): String?{
        return items?.stream()?.collect(Collectors.joining("|"))
    }

    @TypeConverter
    fun toChilds(data: String?) : List<String>?{
        return data?.split("|") ?: listOf()
    }
}