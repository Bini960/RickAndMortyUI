package plat.rickandmorty.acastrom.data.mapper

import plat.rickandmorty.acastrom.data.local.entity.CharacterEntity
import plat.rickandmorty.acastrom.data.model.Character

fun CharacterEntity.toModel(): Character = Character(
    id = id,
    name = name,
    status = status,
    species = species,
    gender = gender,
    image = image,
    isFavorite = isFavorite
)

fun Character.toEntity(): CharacterEntity = CharacterEntity(
    id = id,
    name = name,
    status = status,
    species = species,
    gender = gender,
    image = image,
    isFavorite = isFavorite
)
