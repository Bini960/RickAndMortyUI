package plat.rickandmorty.acastrom.data.remote.api

import plat.rickandmorty.acastrom.data.remote.dto.LocationDto

interface LocationApi {

    suspend fun getLocations(): List<LocationDto>
}
