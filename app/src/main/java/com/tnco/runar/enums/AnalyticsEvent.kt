package com.tnco.runar.enums

enum class AnalyticsEvent(val analyticsName: String) {

    RUNE_OPENED("rune_open"),
    RUNE_VIEWED("rune_viewed"),
    FAVOURITE_OPENED("favourite_opened"),
    LIBRARY_OPENED("library_opened"),
    OB_ABOUT_OPENED("ob1_about_opened"),
    OB_FORTUNE_OPENED("ob2_fortune_opened"),
    OB_INTERPRETATION_OPENED("ob3_interpretation_opened"),
    OB_FAVOURITES_OPENED("ob4_favourites_opened"),
    OB_GENERATOR_OPENED("ob5_generator_opened"),
    OB_LIBRARY_OPENED("ob6_library_opened"),
    OB_NEXT("ob_next"),
    OB_SKIP("ob_skip"),
    OB_START("ob_done_and_start"),
    DRAWS_SELECTED("draws_selected"),
    DRAWS_STARTED("draws_started"),
    INTERPRETATION_STARTED("interpretation_started"),
    INTERPRETATION_VIEWED("interpretation_viewed"),
    DRAWS_SAVED("draws_saved"),
    FAVOURITE_DRAWS_OPENED("favourite_draws_opened"),
    FAVOURITE_DRAWS_DELETED("favourite_draws_deleted"),
    GENERATOR_OPENED("generator_opened"),
    GENERATOR_PATTERN_SELECTED("generator_pattern_selected"),
    GENERATOR_PATTERN_RANDOM_RUNES("generator_pattern_random_runes"),
    GENERATOR_PATTERN_CREATED("generator_pattern_created"),
    GENERATOR_PATTERN_NEW_TYPE("generator_pattern_new_type"),
    GENERATOR_PATTERN_SELECTION_BACKGROUND("generator_pattern_selection_background"),
    GENERATOR_PATTERN_SAVED("generator_pattern_saved"),
    GENERATOR_PATTERN_SHARED("generator_pattern_shared"),
    SCRIPT_INTERRUPTION("script_interruption"),
    MUSIC_LINK_OPENED("music_link_opened"),
    LIBRARY_SECTION_OPENED("library_section_opened")

}