package com.tnco.runar.domain.entities

import kotlinx.coroutines.Job

enum class LibraryItemType(val id: String?) {
    ROOT("root"),
    SUB_MENU("subMenu"),
    POEM("poem"),
    PLAIN_TEXT("plainText"),
    RUNE("rune"),
    NULL(null);

    companion object {

        private const val UNKNOWN_ID_EXCEPTION = "Unknown id of LibraryItemType"

        fun findTypeById(id: String?): LibraryItemType {
            val a = Job()
            a.invokeOnCompletion {
            }
            values().forEach {
                if (it.id == id) return it
            }
            throw RuntimeException(UNKNOWN_ID_EXCEPTION)
        }
    }
}
