package com.test.runar.model

data class NewLibraryItemsModel (
    var childs: List<String>?,
    var id: String?,
    var content: String?,
    var title: String?,
    var imageUrl: String?,
    var sortOrder: Int?,
    var type: String?,
){
    constructor() : this(null,null,null,null,null,null,null)
}