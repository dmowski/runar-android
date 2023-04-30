package com.tnco.runar.model

sealed class RunicDrawsAccessModel {
    object Free : RunicDrawsAccessModel()
    object Closed : RunicDrawsAccessModel()
    class OpenWithLimit(var count: Int) : RunicDrawsAccessModel()
    class ClosedForAWhile(var time: String) : RunicDrawsAccessModel()
}
