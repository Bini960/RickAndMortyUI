package plat.rickandmorty.acastrom.data

import kotlinx.coroutines.delay

class LocationRepository(
    private val locationDb: LocationDb = LocationDb()
) {

    suspend fun getLocations(): List<Location> {
        delay(LIST_LATENCY_MILLIS)
        return locationDb.getAllLocations()
    }

    suspend fun getLocationById(id: Int): Location? {
        delay(DETAIL_LATENCY_MILLIS)
        return locationDb.getAllLocations().firstOrNull { it.id == id }
    }

    private companion object {
        const val LIST_LATENCY_MILLIS = 4_000L
        const val DETAIL_LATENCY_MILLIS = 2_000L
    }
}