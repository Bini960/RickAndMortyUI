package plat.rickandmorty.acastrom.data.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import plat.rickandmorty.acastrom.data.remote.dto.LocationDto
import plat.rickandmorty.acastrom.data.remote.dto.PagedResponseDto

class KtorLocationApi(
    private val client: HttpClient
) : LocationApi {

    override suspend fun getLocations(): List<LocationDto> {
        return client.get(LOCATIONS_PATH).body<PagedResponseDto<LocationDto>>().results
    }

    private companion object {
        const val LOCATIONS_PATH = "location"
    }
}
