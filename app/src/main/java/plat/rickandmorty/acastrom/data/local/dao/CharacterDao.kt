package plat.rickandmorty.acastrom.data.local.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Transaction
import androidx.room3.Update
import androidx.room3.Upsert
import kotlinx.coroutines.flow.Flow
import plat.rickandmorty.acastrom.data.local.entity.CharacterEntity

@Dao
abstract class CharacterDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertAll(characters: List<CharacterEntity>)

    @Insert
    abstract suspend fun insert(character: CharacterEntity)

    @Update
    abstract suspend fun update(character: CharacterEntity)

    @Query("DELETE FROM characters WHERE id = :id")
    abstract suspend fun deleteById(id: Int)

    @Query("SELECT COALESCE(MAX(id), 0) FROM characters")
    abstract suspend fun getMaxId(): Int

    @Upsert
    abstract suspend fun upsertAll(characters: List<CharacterEntity>)

    @Query("SELECT * FROM characters ORDER BY id")
    abstract fun observeAll(): Flow<List<CharacterEntity>>

    @Query("SELECT * FROM characters WHERE id = :id")
    abstract fun observeById(id: Int): Flow<CharacterEntity?>

    @Query("SELECT id FROM characters WHERE is_favorite = 1")
    abstract suspend fun getFavoriteIds(): List<Int>

    @Query("UPDATE characters SET is_favorite = :isFavorite WHERE id = :id")
    abstract suspend fun updateFavorite(id: Int, isFavorite: Boolean)

    @Query("SELECT COUNT(*) FROM characters")
    abstract suspend fun count(): Int

    @Query("DELETE FROM characters")
    abstract suspend fun deleteAll()

    @Transaction
    open suspend fun insertWithNextId(character: CharacterEntity): Int {
        val newId = getMaxId() + 1
        insert(character.copy(id = newId))
        return newId
    }

    @Transaction
    open suspend fun upsertKeepingFavorites(characters: List<CharacterEntity>) {
        val favoriteIds = getFavoriteIds().toSet()
        upsertAll(characters.map { it.copy(isFavorite = it.id in favoriteIds) })
    }
}
