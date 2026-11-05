package plat.rickandmorty.acastrom.data.repository

import kotlinx.coroutines.flow.Flow
import plat.rickandmorty.acastrom.data.model.Location

interface LocationRepository {

    fun observeLocations(): Flow<List<Location>>

    fun observeLocationById(id: Int): Flow<Location?>

    suspend fun clearLocalData()
}
