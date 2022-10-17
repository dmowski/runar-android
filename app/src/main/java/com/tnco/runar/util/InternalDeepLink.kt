package com.tnco.runar.util

sealed class InternalDeepLink(private val route: String) {

    object ConnectivityErrorFragment : InternalDeepLink("runar-android://ConnectivityErrorFragment")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}