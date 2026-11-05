package plat.rickandmorty.acastrom.data.repository

import kotlinx.coroutines.flow.Flow
import plat.rickandmorty.acastrom.data.model.Character
import plat.rickandmorty.acastrom.data.model.CharacterDraft

interface CharacterRepository {

    fun observeCharacters(): Flow<List<Character>>

    fun observeCharacterById(id: Int): Flow<Character?>

    suspend fun refreshFromApi()

    suspend fun createCharacter(draft: CharacterDraft): Int

    suspend fun updateCharacter(id: Int, draft: CharacterDraft)

    suspend fun deleteCharacter(id: Int)

    suspend fun updateFavorite(id: Int, isFavorite: Boolean)

    suspend fun clearLocalData()
}
