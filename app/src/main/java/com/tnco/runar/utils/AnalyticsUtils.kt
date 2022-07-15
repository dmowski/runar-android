package com.tnco.runar.utils

import com.tnco.runar.utils.AnalyticsConstants.BRIEF_PROPHECY
import com.tnco.runar.utils.AnalyticsConstants.CELTIC_CROSS
import com.tnco.runar.utils.AnalyticsConstants.CROSS
import com.tnco.runar.utils.AnalyticsConstants.CROSS_OF_ELEMENTS
import com.tnco.runar.utils.AnalyticsConstants.DOES_NOT_EXIST
import com.tnco.runar.utils.AnalyticsConstants.NORNS
import com.tnco.runar.utils.AnalyticsConstants.RUNE_OF_THE_DAY
import com.tnco.runar.utils.AnalyticsConstants.THORS_HAMMER
import com.tnco.runar.utils.AnalyticsConstants.TWO_RUNES

object AnalyticsUtils {

    fun convertLayoutIdToName(id: Int): String {
        val layoutName = when (id) {
            1 -> RUNE_OF_THE_DAY
            2 -> TWO_RUNES
            3 -> NORNS
            4 -> BRIEF_PROPHECY
            5 -> THORS_HAMMER
            6 -> CROSS
            7 -> CROSS_OF_ELEMENTS
            8 -> CELTIC_CROSS
            else -> DOES_NOT_EXIST
        }
        return layoutName
    }
}
