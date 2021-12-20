package com.tnco.runar.model

import com.tnco.runar.retrofit.En
import com.tnco.runar.retrofit.Ru

data class RunesItemsModel(
    var id: Int,
    var imgUrl: String?,
    var enDesc: String?,
    var enTitle: String?,
    var ruDesc: String?,
    var ruTitle: String?
)
