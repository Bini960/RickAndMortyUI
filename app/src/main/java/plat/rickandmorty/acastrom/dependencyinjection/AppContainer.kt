package plat.rickandmorty.acastrom.dependencyinjection

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room3.Room
import io.ktor.client.HttpClient
import plat.rickandmorty.acastrom.data.CharacterDb
import plat.rickandmorty.acastrom.data.LocationDb
import plat.rickandmorty.acastrom.data.SyncRepository
import plat.rickandmorty.acastrom.data.local.AppDatabase
import plat.rickandmorty.acastrom.data.remote.HttpClientFactory
import plat.rickandmorty.acastrom.data.remote.api.CharacterApi
import plat.rickandmorty.acastrom.data.remote.api.KtorCharacterApi
import plat.rickandmorty.acastrom.data.remote.api.KtorLocationApi
import plat.rickandmorty.acastrom.data.remote.api.LocationApi
import plat.rickandmorty.acastrom.data.repository.CharacterRepository
import plat.rickandmorty.acastrom.data.repository.CharacterRepositoryImpl
import plat.rickandmorty.acastrom.data.repository.LocationRepository
import plat.rickandmorty.acastrom.data.repository.LocationRepositoryImpl
import plat.rickandmorty.acastrom.data.repository.SessionRepository
import plat.rickandmorty.acastrom.data.repository.SessionRepositoryImpl

private val Context.sessionDataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class AppContainer(context: Context) {

    private val database: AppDatabase by lazy {
        Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    private val httpClient: HttpClient by lazy {
        HttpClientFactory.create()
    }

    private val characterApi: CharacterApi by lazy {
        KtorCharacterApi(httpClient)
    }

    private val locationApi: LocationApi by lazy {
        KtorLocationApi(httpClient)
    }

    val sessionRepository: SessionRepository = SessionRepositoryImpl(context.sessionDataStore)

    val syncRepository: SyncRepository by lazy {
        SyncRepository(
            characterDao = database.characterDao(),
            locationDao = database.locationDao(),
            characterDb = CharacterDb(),
            locationDb = LocationDb()
        )
    }

    val characterRepository: CharacterRepository by lazy {
        CharacterRepositoryImpl(database.characterDao())
    }

    val locationRepository: LocationRepository by lazy {
        LocationRepositoryImpl(database.locationDao())
    }

    private companion object {
        const val DATABASE_NAME = "rickandmorty.db"
    }
}
