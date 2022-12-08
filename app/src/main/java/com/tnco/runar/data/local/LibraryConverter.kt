package com.tnco.runar.data.local

import androidx.room.TypeConverter
import java.util.stream.Collectors

class LibraryConverter {

    @TypeConverter
    fun fromList(items: List<String>?): String? {
        return items?.stream()?.collect(Collectors.joining("|"))
    }

    @TypeConverter
    fun toList(data: String?): List<String> {
        return data?.split("|") ?: listOf()
    }
}
