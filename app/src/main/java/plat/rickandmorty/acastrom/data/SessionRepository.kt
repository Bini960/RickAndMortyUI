package plat.rickandmorty.acastrom.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class SessionRepository(
    private val dataStore: DataStore<Preferences>
) {

    val userName: Flow<String?> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[NAME_KEY]?.takeIf { it.isNotBlank() }
        }

    suspend fun saveUserName(name: String) {
        val trimmedName = name.trim()
        require(trimmedName.isNotEmpty()) { "El nombre no puede estar vacío" }
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = trimmedName
        }
    }

    suspend fun clearUserName() {
        dataStore.edit { preferences ->
            preferences.remove(NAME_KEY)
        }
    }

    private companion object {
        val NAME_KEY = stringPreferencesKey("name")
    }
}