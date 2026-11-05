package plat.rickandmorty.acastrom.data.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import plat.rickandmorty.acastrom.data.remote.dto.CharacterDto
import plat.rickandmorty.acastrom.data.remote.dto.PagedResponseDto

class KtorCharacterApi(
    private val client: HttpClient
) : CharacterApi {

    override suspend fun getCharacters(): List<CharacterDto> {
        return client.get(CHARACTERS_PATH).body<PagedResponseDto<CharacterDto>>().results
    }

    private companion object {
        const val CHARACTERS_PATH = "character"
    }
}
