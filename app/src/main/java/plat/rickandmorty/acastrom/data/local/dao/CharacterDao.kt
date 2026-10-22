package plat.rickandmorty.acastrom.data.local.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import kotlinx.coroutines.flow.Flow
import plat.rickandmorty.acastrom.data.local.entity.CharacterEntity

@Dao
interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(characters: List<CharacterEntity>)

    @Query("SELECT * FROM characters ORDER BY id")
    fun observeAll(): Flow<List<CharacterEntity>>

    @Query("SELECT * FROM characters WHERE id = :id")
    fun observeById(id: Int): Flow<CharacterEntity?>

    @Query("UPDATE characters SET is_favorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: Int, isFavorite: Boolean)

    @Query("SELECT COUNT(*) FROM characters")
    suspend fun count(): Int

    @Query("DELETE FROM characters")
    suspend fun deleteAll()
}
