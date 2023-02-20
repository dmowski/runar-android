package com.tnco.runar.di.annotations

import javax.inject.Qualifier

@Qualifier
annotation class MainDispatcher

@Qualifier
annotation class IoDispatcher

@Qualifier
annotation class DefaultDispatcher

@Qualifier
annotation class UnconfinedDispatcher
