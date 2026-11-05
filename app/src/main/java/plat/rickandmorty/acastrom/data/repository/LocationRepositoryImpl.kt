package plat.rickandmorty.acastrom.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import plat.rickandmorty.acastrom.data.local.dao.LocationDao
import plat.rickandmorty.acastrom.data.mapper.toEntity
import plat.rickandmorty.acastrom.data.mapper.toModel
import plat.rickandmorty.acastrom.data.model.Location
import plat.rickandmorty.acastrom.data.remote.api.LocationApi
import kotlin.coroutines.cancellation.CancellationException

class LocationRepositoryImpl(
    private val locationApi: LocationApi,
    private val locationDao: LocationDao
) : LocationRepository {

    override fun observeLocations(): Flow<List<Location>> = flow {
        refreshFromApi()
        emitAll(locationDao.observeAll().map { entities -> entities.map { it.toModel() } })
    }

    override fun observeLocationById(id: Int): Flow<Location?> {
        return locationDao.observeById(id).map { entity -> entity?.toModel() }
    }

    private suspend fun refreshFromApi() {
        val locations = try {
            locationApi.getLocations()
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            if (locationDao.count() == 0) throw exception
            return
        }
        locationDao.upsertAll(locations.map { it.toEntity() })
    }
}
