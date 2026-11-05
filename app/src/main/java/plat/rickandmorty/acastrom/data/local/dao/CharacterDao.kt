package plat.rickandmorty.acastrom.data.local.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Transaction
import kotlinx.coroutines.flow.Flow
import plat.rickandmorty.acastrom.data.local.entity.CharacterEntity

@Dao
abstract class CharacterDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertAll(characters: List<CharacterEntity>)

    @Insert
    abstract suspend fun insert(character: CharacterEntity)

    @Query("SELECT * FROM characters ORDER BY id")
    abstract fun observeAll(): Flow<List<CharacterEntity>>

    @Query("SELECT * FROM characters WHERE id = :id")
    abstract fun observeById(id: Int): Flow<CharacterEntity?>

    @Query("UPDATE characters SET name = :name, status = :status, species = :species, gender = :gender, image = :image WHERE id = :id")
    abstract suspend fun updateDetails(
        id: Int,
        name: String,
        status: String,
        species: String,
        gender: String,
        image: String
    )

    @Query("UPDATE characters SET is_favorite = :isFavorite WHERE id = :id")
    abstract suspend fun updateFavorite(id: Int, isFavorite: Boolean)

    @Query("DELETE FROM characters WHERE id = :id")
    abstract suspend fun deleteById(id: Int)

    @Query("DELETE FROM characters")
    abstract suspend fun deleteAll()

    @Query("SELECT COALESCE(MAX(id), 0) FROM characters")
    abstract suspend fun getMaxId(): Int

    @Transaction
    open suspend fun insertWithNextId(character: CharacterEntity): Int {
        val newId = getMaxId() + 1
        insert(character.copy(id = newId))
        return newId
    }
}
