package com.tnco.runar.util

import com.tnco.runar.util.AnalyticsConstants.BRIEF_PROPHECY
import com.tnco.runar.util.AnalyticsConstants.CELTIC_CROSS
import com.tnco.runar.util.AnalyticsConstants.CROSS
import com.tnco.runar.util.AnalyticsConstants.CROSS_OF_ELEMENTS
import com.tnco.runar.util.AnalyticsConstants.DOES_NOT_EXIST
import com.tnco.runar.util.AnalyticsConstants.NORNS
import com.tnco.runar.util.AnalyticsConstants.RUNE_OF_THE_DAY
import com.tnco.runar.util.AnalyticsConstants.THORS_HAMMER
import com.tnco.runar.util.AnalyticsConstants.TWO_RUNES

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
