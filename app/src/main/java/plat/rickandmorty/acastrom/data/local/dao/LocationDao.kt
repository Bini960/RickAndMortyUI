package plat.rickandmorty.acastrom.data.local.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import kotlinx.coroutines.flow.Flow
import plat.rickandmorty.acastrom.data.local.entity.LocationEntity

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(locations: List<LocationEntity>)

    @Query("SELECT * FROM locations ORDER BY id")
    fun observeAll(): Flow<List<LocationEntity>>

    @Query("SELECT * FROM locations WHERE id = :id")
    fun observeById(id: Int): Flow<LocationEntity?>

    @Query("SELECT COUNT(*) FROM locations")
    suspend fun count(): Int

    @Query("DELETE FROM locations")
    suspend fun deleteAll()
}
