package plat.rickandmorty.acastrom.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import plat.rickandmorty.acastrom.data.local.dao.CharacterDao
import plat.rickandmorty.acastrom.data.mapper.toEntity
import plat.rickandmorty.acastrom.data.mapper.toModel
import plat.rickandmorty.acastrom.data.model.Character
import plat.rickandmorty.acastrom.data.model.CharacterDraft
import plat.rickandmorty.acastrom.data.remote.api.CharacterApi

class CharacterRepositoryImpl(
    private val characterApi: CharacterApi,
    private val characterDao: CharacterDao
) : CharacterRepository {

    override fun observeCharacters(): Flow<List<Character>> {
        return characterDao.observeAll().map { entities -> entities.map { it.toModel() } }
    }

    override fun observeCharacterById(id: Int): Flow<Character?> {
        return characterDao.observeById(id).map { entity -> entity?.toModel() }
    }

    override suspend fun refreshFromApi() {
        val characters = characterApi.getCharacters()
        characterDao.insertAll(characters.map { it.toEntity() })
    }

    override suspend fun createCharacter(draft: CharacterDraft): Int {
        return characterDao.insertWithNextId(draft.toEntity())
    }

    override suspend fun updateCharacter(id: Int, draft: CharacterDraft) {
        characterDao.updateDetails(
            id = id,
            name = draft.name,
            status = draft.status,
            species = draft.species,
            gender = draft.gender,
            image = draft.image
        )
    }

    override suspend fun deleteCharacter(id: Int) {
        characterDao.deleteById(id)
    }

    override suspend fun updateFavorite(id: Int, isFavorite: Boolean) {
        characterDao.updateFavorite(id, isFavorite)
    }

    override suspend fun clearLocalData() {
        characterDao.deleteAll()
    }
}
