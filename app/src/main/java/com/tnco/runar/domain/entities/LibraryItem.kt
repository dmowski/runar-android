package com.tnco.runar.domain.entities

import com.tnco.runar.model.LibraryItemsModel

data class LibraryItem(
    val id: String,
    val childs: List<String> = listOf(),
    val content: String = EMPTY_STRING,
    val title: String = EMPTY_STRING,
    val imageUrl: String = EMPTY_STRING,
    val sortOrder: Int = DEFAULT_INT,
    val type: LibraryItemType,
    val linkTitle: String = EMPTY_STRING,
    val linkUrl: String = EMPTY_STRING,
    val audioUrl: String = EMPTY_STRING,
    val audioDuration: Int = DEFAULT_INT,
    val runeTags: List<String> = listOf()
) {

    companion object { // TODO replace by mapper

        private const val EMPTY_STRING = ""
        private const val DEFAULT_INT = 0

        fun fromLibraryItemsModel(libraryItemsModel: LibraryItemsModel) = LibraryItem(
            id = libraryItemsModel.id,
            childs = libraryItemsModel.childs ?: listOf(),
            content = libraryItemsModel.content ?: EMPTY_STRING,
            title = libraryItemsModel.title ?: EMPTY_STRING,
            imageUrl = libraryItemsModel.imageUrl ?: EMPTY_STRING,
            sortOrder = libraryItemsModel.sortOrder ?: DEFAULT_INT,
            type = LibraryItemType.findTypeById(libraryItemsModel.type),
            linkTitle = libraryItemsModel.linkTitle ?: EMPTY_STRING,
            linkUrl = libraryItemsModel.linkUrl ?: EMPTY_STRING,
            audioUrl = libraryItemsModel.audioUrl ?: EMPTY_STRING,
            audioDuration = libraryItemsModel.audioDuration ?: DEFAULT_INT,
            runeTags = libraryItemsModel.runeTags ?: listOf(),
        )
    }
}
