package plat.rickandmorty.acastrom.data

import kotlinx.coroutines.delay
import plat.rickandmorty.acastrom.data.local.dao.CharacterDao
import plat.rickandmorty.acastrom.data.local.dao.LocationDao
import plat.rickandmorty.acastrom.data.local.mapper.toEntity

class SyncRepository(
    private val characterDao: CharacterDao,
    private val locationDao: LocationDao,
    private val characterDb: CharacterDb,
    private val locationDb: LocationDb
) {

    suspend fun isSynced(): Boolean {
        return characterDao.count() > 0 && locationDao.count() > 0
    }

    suspend fun syncIfNeeded() {
        if (isSynced()) return
        delay(SYNC_LATENCY_MILLIS)
        characterDao.insertAll(characterDb.getAllCharacters().map { it.toEntity() })
        locationDao.insertAll(locationDb.getAllLocations().map { it.toEntity() })
    }

    suspend fun clearAll() {
        characterDao.deleteAll()
        locationDao.deleteAll()
    }

    private companion object {
        const val SYNC_LATENCY_MILLIS = 4_000L
    }
}
