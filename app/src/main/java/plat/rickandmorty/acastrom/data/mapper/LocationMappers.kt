package plat.rickandmorty.acastrom.data.mapper

import plat.rickandmorty.acastrom.data.local.entity.LocationEntity
import plat.rickandmorty.acastrom.data.model.Location

fun LocationEntity.toModel(): Location = Location(
    id = id,
    name = name,
    type = type,
    dimension = dimension
)

fun Location.toEntity(): LocationEntity = LocationEntity(
    id = id,
    name = name,
    type = type,
    dimension = dimension
)
