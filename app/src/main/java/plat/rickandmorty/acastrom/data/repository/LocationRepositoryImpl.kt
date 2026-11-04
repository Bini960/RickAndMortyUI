package plat.rickandmorty.acastrom.data.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import plat.rickandmorty.acastrom.data.local.dao.LocationDao
import plat.rickandmorty.acastrom.data.mapper.toModel
import plat.rickandmorty.acastrom.data.model.Location

class LocationRepositoryImpl(
    private val locationDao: LocationDao
) : LocationRepository {

    override fun observeLocations(): Flow<List<Location>> {
        return locationDao.observeAll()
            .map { entities -> entities.map { it.toModel() } }
            .onStart { delay(LIST_LATENCY_MILLIS) }
    }

    override fun observeLocationById(id: Int): Flow<Location?> {
        return locationDao.observeById(id)
            .map { entity -> entity?.toModel() }
            .onStart { delay(DETAIL_LATENCY_MILLIS) }
    }

    private companion object {
        const val LIST_LATENCY_MILLIS = 4_000L
        const val DETAIL_LATENCY_MILLIS = 2_000L
    }
}
