package plat.rickandmorty.acastrom.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import plat.rickandmorty.acastrom.data.local.dao.CharacterDao
import plat.rickandmorty.acastrom.data.mapper.toEntity
import plat.rickandmorty.acastrom.data.mapper.toModel
import plat.rickandmorty.acastrom.data.model.Character
import plat.rickandmorty.acastrom.data.remote.api.CharacterApi
import kotlin.coroutines.cancellation.CancellationException

class CharacterRepositoryImpl(
    private val characterApi: CharacterApi,
    private val characterDao: CharacterDao
) : CharacterRepository {

    override fun observeCharacters(): Flow<List<Character>> = flow {
        refreshFromApi()
        emitAll(characterDao.observeAll().map { entities -> entities.map { it.toModel() } })
    }

    override fun observeCharacterById(id: Int): Flow<Character?> {
        return characterDao.observeById(id).map { entity -> entity?.toModel() }
    }

    override suspend fun updateFavorite(id: Int, isFavorite: Boolean) {
        characterDao.updateFavorite(id, isFavorite)
    }

    override suspend fun clearLocalData() {
        characterDao.deleteAll()
    }

    private suspend fun refreshFromApi() {
        val characters = try {
            characterApi.getCharacters()
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            if (characterDao.count() == 0) throw exception
            return
        }
        characterDao.upsertKeepingFavorites(characters.map { it.toEntity() })
    }
}
