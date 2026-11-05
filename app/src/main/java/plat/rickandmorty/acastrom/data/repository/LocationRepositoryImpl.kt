package plat.rickandmorty.acastrom.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import plat.rickandmorty.acastrom.data.local.dao.LocationDao
import plat.rickandmorty.acastrom.data.mapper.toEntity
import plat.rickandmorty.acastrom.data.mapper.toModel
import plat.rickandmorty.acastrom.data.model.Location
import plat.rickandmorty.acastrom.data.remote.api.LocationApi

class LocationRepositoryImpl(
    private val locationApi: LocationApi,
    private val locationDao: LocationDao
) : LocationRepository {

    override fun observeLocations(): Flow<List<Location>> {
        return locationDao.observeAll().map { entities -> entities.map { it.toModel() } }
    }

    override fun observeLocationById(id: Int): Flow<Location?> {
        return locationDao.observeById(id).map { entity -> entity?.toModel() }
    }

    override suspend fun refreshFromApi() {
        val locations = locationApi.getLocations()
        locationDao.insertAll(locations.map { it.toEntity() })
    }

    override suspend fun updateFavorite(id: Int, isFavorite: Boolean) {
        locationDao.updateFavorite(id, isFavorite)
    }

    override suspend fun clearLocalData() {
        locationDao.deleteAll()
    }
}
