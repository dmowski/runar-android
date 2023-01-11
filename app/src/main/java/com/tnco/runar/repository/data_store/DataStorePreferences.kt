package com.tnco.runar.repository.data_store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.tnco.runar.model.DeveloperSwitcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object DataStorePreferences {

    private const val PREFERENCE_NAME = "settings"
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCE_NAME)
    private val SWITCHERS_STATES = stringSetPreferencesKey("switchers_states")
    private lateinit var dataStore: DataStore<Preferences>

    fun init(context: Context) {
        dataStore = context.dataStore
    }

    /**
     * Add your switchers to the list.
     * Of significant note: This switchers are available in debug mode only.
     * Example: DeveloperSwitcher(name = "test", state = false)
     * @param name displayed switcher text
     * @param state initial state
     */
    suspend fun initialPopulate() {
        saveSwitchers(
            listOf(
                DeveloperSwitcher(name = "Audio fairy tales displaying", state = true)
            )
        )
    }

    val switchers: Flow<List<DeveloperSwitcher>>
        get() = dataStore.data.map { preferences ->
            val switchers = mutableListOf<DeveloperSwitcher>()
            val switcherNames = preferences[SWITCHERS_STATES]
            switcherNames?.let {
                it.forEach { name ->
                    val state = preferences[booleanPreferencesKey(name)] ?: false
                    switchers.add(DeveloperSwitcher(name = name, state = state))
                }
            }
            switchers
        }

    private suspend fun saveSwitchers(switchers: List<DeveloperSwitcher>) {
        dataStore.edit { preferences ->
            val states = switchers
                .map { it.name }
                .toSet()
            preferences[SWITCHERS_STATES] = states
            switchers.forEach {
                preferences[booleanPreferencesKey(it.name)] = it.state
            }
        }
    }

    suspend fun saveSwitcher(switcher: DeveloperSwitcher) {
        dataStore.edit { preferences ->
            preferences[booleanPreferencesKey(switcher.name)] = switcher.state
        }
    }

    suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
