package plat.rickandmorty.acastrom.data.repository

import kotlinx.coroutines.flow.Flow

interface SessionRepository {

    val userName: Flow<String?>

    suspend fun saveUserName(name: String)

    suspend fun clearUserName()
}
