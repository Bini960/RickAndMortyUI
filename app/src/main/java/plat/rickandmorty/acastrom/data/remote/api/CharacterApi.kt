package plat.rickandmorty.acastrom.data.remote.api

import plat.rickandmorty.acastrom.data.remote.dto.CharacterDto

interface CharacterApi {

    suspend fun getCharacters(): List<CharacterDto>
}
