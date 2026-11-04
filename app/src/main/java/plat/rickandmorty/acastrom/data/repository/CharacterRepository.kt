package plat.rickandmorty.acastrom.data.repository

import kotlinx.coroutines.flow.Flow
import plat.rickandmorty.acastrom.data.model.Character

interface CharacterRepository {

    fun observeCharacters(): Flow<List<Character>>

    fun observeCharacterById(id: Int): Flow<Character?>

    suspend fun updateFavorite(id: Int, isFavorite: Boolean)
}
