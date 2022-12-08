package com.tnco.runar.repository.backend

import com.tnco.runar.data.remote.response.LibraryResponse
import com.tnco.runar.data.remote.response.RunesResponse
import com.tnco.runar.model.LibraryItemsModel
import com.tnco.runar.model.RunesItemsModel

object DataClassConverters {
    fun libRespToItems(respList: List<LibraryResponse>): List<LibraryItemsModel> {
        val resultList: ArrayList<LibraryItemsModel> = arrayListOf()
        for (item in respList) {
            val itemModel = LibraryItemsModel(
                id = item._id!!,
                childs = item.childIds,
                content = item.content,
                title = item.title,
                imageUrl = item.imageUrl,
                sortOrder = item.sortOrder,
                type = item.type,
                linkTitle = item.linkTitle,
                linkUrl = item.linkUrl,
                audioUrl = item.audioUrl,
                audioDuration = item.audioDuration,
                runeTags = item.tags
            )
            resultList.add(itemModel)
        }
        return resultList
    }

    fun runesRespToItems(respList: List<RunesResponse>): List<RunesItemsModel> {
        val resultList: ArrayList<RunesItemsModel> = arrayListOf()
        for (item in respList) {
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
