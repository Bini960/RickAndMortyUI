package plat.rickandmorty.acastrom.data.local

import androidx.room3.AutoMigration
import androidx.room3.Database
import androidx.room3.RoomDatabase
import plat.rickandmorty.acastrom.data.local.dao.CharacterDao
import plat.rickandmorty.acastrom.data.local.dao.LocationDao
import plat.rickandmorty.acastrom.data.local.entity.CharacterEntity
import plat.rickandmorty.acastrom.data.local.entity.LocationEntity

@Database(
    entities = [CharacterEntity::class, LocationEntity::class],
    version = 2,
    autoMigrations = [AutoMigration(from = 1, to = 2)]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
    abstract fun locationDao(): LocationDao
}
