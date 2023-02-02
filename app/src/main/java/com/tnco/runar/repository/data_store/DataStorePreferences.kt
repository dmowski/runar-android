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

    private val languageKey = stringPreferencesKey("language")
    private val switcherStates = stringSetPreferencesKey("switchers_states")

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
                DeveloperSwitcher(name = AUDIO_SWITCHER_NAME, state = true)
            )
        )
    }

    val switchers: Flow<List<DeveloperSwitcher>>
        get() = dataStore.data.map { preferences ->
            val switchers = mutableListOf<DeveloperSwitcher>()
            val switcherNames = preferences[switcherStates]
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
            preferences[switcherStates] = states
            switchers.forEach {
                preferences[booleanPreferencesKey(it.name)] = it.state
            }
        }
    }

    val appLanguage: Flow<String>
        get() = dataStore.data.map { preferences ->
            preferences[languageKey] ?: "ru"
        }

    suspend fun saveAppLanguage(language: String) {
        dataStore.edit { preferences ->
            preferences[languageKey] = language
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

    companion object {
        const val AUDIO_SWITCHER_NAME = "Audio fairy tales displaying"
    }
}
