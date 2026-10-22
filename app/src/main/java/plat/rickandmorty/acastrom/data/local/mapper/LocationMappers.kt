package plat.rickandmorty.acastrom.data.local.mapper

import plat.rickandmorty.acastrom.data.Location
import plat.rickandmorty.acastrom.data.local.entity.LocationEntity

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
