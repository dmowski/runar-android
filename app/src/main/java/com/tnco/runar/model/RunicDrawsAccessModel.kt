package com.tnco.runar.model

import androidx.compose.runtime.State

sealed class RunicDrawsAccessModel {
    object Free : RunicDrawsAccessModel()
    object Closed : RunicDrawsAccessModel()
    class OpenWithLimit(var count: State<Int>) : RunicDrawsAccessModel()
    class ClosedForAWhile(var time: State<String>) : RunicDrawsAccessModel()
}
