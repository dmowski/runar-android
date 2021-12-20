package com.tnco.runar.converters

import com.tnco.runar.model.LibraryItemsModel
import com.tnco.runar.model.RunesItemsModel
import com.tnco.runar.retrofit.LibraryResponse
import com.tnco.runar.retrofit.RunesResponse

object DataClassConverters {
    fun libRespToItems(respList: List<LibraryResponse>): List<LibraryItemsModel>{
        val resultList: ArrayList<LibraryItemsModel> = arrayListOf()
        for(item in respList){
            val itemModel = LibraryItemsModel(
                id = item._id!!,
                childs = item.childIds,
                content = item.content,
                title = item.title,
                imageUrl = item.imageUrl,
                sortOrder = item.sortOrder,
                type = item.type,
                linkTitle = item.linkTitle,
                linkUrl = item.linkUrl
            )
            resultList.add(itemModel)
        }
        return resultList
    }

    fun runesRespToItems(respList: List<RunesResponse>): List<RunesItemsModel>{
        val resultList: ArrayList<RunesItemsModel> = arrayListOf()
        for(item in respList){
            val itemModel = RunesItemsModel(
                id = item.id!!,
                imgUrl = item.imgUrl,
                enDesc = item.en?.description,
                enTitle = item.en?.title,
                ruDesc = item.ru?.description,
                ruTitle = item.ru?.title
            )
            resultList.add(itemModel)
        }
        return resultList
    }
}