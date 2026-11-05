package plat.rickandmorty.acastrom.data.mapper

import plat.rickandmorty.acastrom.data.local.entity.LocationEntity
import plat.rickandmorty.acastrom.data.model.Location
import plat.rickandmorty.acastrom.data.remote.dto.LocationDto

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

fun LocationDto.toEntity(): LocationEntity = LocationEntity(
    id = id,
    name = name,
    type = type,
    dimension = dimension
)
