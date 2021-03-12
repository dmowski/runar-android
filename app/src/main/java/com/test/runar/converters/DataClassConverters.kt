package com.test.runar.converters

import com.test.runar.model.LibraryItemsModel
import com.test.runar.retrofit.LibraryResponse

object DataClassConverters {
    fun LibRespToItems(respList: List<LibraryResponse>): List<LibraryItemsModel>{
        val resultList: ArrayList<LibraryItemsModel> = arrayListOf()
        for(item in respList){
            val itemModel = LibraryItemsModel(
                id = item._id!!,
                childs = item.childIds,
                content = item.content,
                title = item.title,
                imageUrl = item.imageUrl,
                sortOrder = item.sortOrder,
                type = item.type
            )
            resultList.add(itemModel)
        }
        return resultList
    }
}