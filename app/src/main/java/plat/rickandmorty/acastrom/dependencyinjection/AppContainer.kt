package plat.rickandmorty.acastrom.dependencyinjection

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import plat.rickandmorty.acastrom.data.SessionRepository

private val Context.sessionDataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class AppContainer(context: Context) {
    val sessionRepository: SessionRepository = SessionRepository(context.sessionDataStore)
}