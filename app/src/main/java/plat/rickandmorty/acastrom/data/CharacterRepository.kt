package plat.rickandmorty.acastrom.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import plat.rickandmorty.acastrom.data.local.dao.CharacterDao
import plat.rickandmorty.acastrom.data.local.mapper.toModel

class CharacterRepository(
    private val characterDao: CharacterDao
) {

    fun observeCharacters(): Flow<List<Character>> {
        return characterDao.observeAll()
            .map { entities -> entities.map { it.toModel() } }
            .onStart { delay(LIST_LATENCY_MILLIS) }
    }

    fun observeCharacterById(id: Int): Flow<Character?> {
        return characterDao.observeById(id)
            .map { entity -> entity?.toModel() }
            .onStart { delay(DETAIL_LATENCY_MILLIS) }
    }

    private companion object {
        const val LIST_LATENCY_MILLIS = 4_000L
        const val DETAIL_LATENCY_MILLIS = 2_000L
    }
}
