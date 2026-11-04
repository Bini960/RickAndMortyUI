package plat.rickandmorty.acastrom.data.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import plat.rickandmorty.acastrom.data.local.dao.CharacterDao
import plat.rickandmorty.acastrom.data.mapper.toModel
import plat.rickandmorty.acastrom.data.model.Character

class CharacterRepositoryImpl(
    private val characterDao: CharacterDao
) : CharacterRepository {

    override fun observeCharacters(): Flow<List<Character>> {
        return characterDao.observeAll()
            .map { entities -> entities.map { it.toModel() } }
            .onStart { delay(LIST_LATENCY_MILLIS) }
    }

    override fun observeCharacterById(id: Int): Flow<Character?> {
        return characterDao.observeById(id)
            .map { entity -> entity?.toModel() }
            .onStart { delay(DETAIL_LATENCY_MILLIS) }
    }

    override suspend fun updateFavorite(id: Int, isFavorite: Boolean) {
        characterDao.updateFavorite(id, isFavorite)
    }

    private companion object {
        const val LIST_LATENCY_MILLIS = 4_000L
        const val DETAIL_LATENCY_MILLIS = 2_000L
    }
}
