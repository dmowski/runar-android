package com.tnco.runar.repository.data_store

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.tnco.runar.model.DeveloperSwitcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStorePreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    /**
     * Add your switchers to the list.
     * Of significant note: This switchers are available in debug mode only.
     * Example: DeveloperSwitcher(name = "test", state = false)
     * @param name displayed switcher text
     * @param state initial state
     */
    private val initialSwitchers = listOf(
        DeveloperSwitcher(name = AUDIO_SWITCHER_NAME, state = true)
    )

    suspend fun initialPopulate() {
        dataStore.edit { preferences ->
            initialSwitchers.forEach { switcher ->
                if (!preferences.contains(booleanPreferencesKey(switcher.name)))
                    preferences[booleanPreferencesKey(switcher.name)] = switcher.state
            }
        }
    }

    val switchers: Flow<List<DeveloperSwitcher>>
        get() = dataStore.data.map { preferences ->
            val switchers = mutableListOf<DeveloperSwitcher>()
            initialSwitchers.forEach { switcher ->
                val state = preferences[booleanPreferencesKey(switcher.name)] ?: switcher.state
                switchers.add(DeveloperSwitcher(name = switcher.name, state = state))
            }
            switchers
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

    companion object {
        const val AUDIO_SWITCHER_NAME = "Audio fairy tales displaying"
    }
}
