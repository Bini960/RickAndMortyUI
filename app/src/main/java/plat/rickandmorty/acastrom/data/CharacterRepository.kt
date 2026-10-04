package plat.rickandmorty.acastrom.data

import kotlinx.coroutines.delay

class CharacterRepository(
    private val characterDb: CharacterDb = CharacterDb()
) {

    suspend fun getCharacters(): List<Character> {
        delay(LIST_LATENCY_MILLIS)
        return characterDb.getAllCharacters()
    }

    suspend fun getCharacterById(id: Int): Character? {
        delay(DETAIL_LATENCY_MILLIS)
        return characterDb.getAllCharacters().firstOrNull { it.id == id }
    }

    private companion object {
        const val LIST_LATENCY_MILLIS = 4_000L
        const val DETAIL_LATENCY_MILLIS = 2_000L
    }
}