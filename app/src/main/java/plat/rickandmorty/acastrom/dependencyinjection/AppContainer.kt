package plat.rickandmorty.acastrom.dependencyinjection

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room3.Room
import plat.rickandmorty.acastrom.data.CharacterDb
import plat.rickandmorty.acastrom.data.CharacterRepository
import plat.rickandmorty.acastrom.data.LocationDb
import plat.rickandmorty.acastrom.data.LocationRepository
import plat.rickandmorty.acastrom.data.SessionRepository
import plat.rickandmorty.acastrom.data.SyncRepository
import plat.rickandmorty.acastrom.data.local.AppDatabase

private val Context.sessionDataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class AppContainer(context: Context) {

    private val database: AppDatabase by lazy {
        Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    val sessionRepository: SessionRepository = SessionRepository(context.sessionDataStore)

    val syncRepository: SyncRepository by lazy {
        SyncRepository(
            characterDao = database.characterDao(),
            locationDao = database.locationDao(),
            characterDb = CharacterDb(),
            locationDb = LocationDb()
        )
    }

    val characterRepository: CharacterRepository by lazy {
        CharacterRepository(database.characterDao())
    }

    val locationRepository: LocationRepository by lazy {
        LocationRepository(database.locationDao())
    }

    private companion object {
        const val DATABASE_NAME = "rickandmorty.db"
    }
}
