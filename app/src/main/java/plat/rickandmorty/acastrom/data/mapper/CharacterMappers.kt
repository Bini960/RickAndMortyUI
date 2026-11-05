package plat.rickandmorty.acastrom.data.mapper

import plat.rickandmorty.acastrom.data.local.entity.CharacterEntity
import plat.rickandmorty.acastrom.data.model.Character
import plat.rickandmorty.acastrom.data.model.CharacterDraft
import plat.rickandmorty.acastrom.data.remote.dto.CharacterDto

fun CharacterEntity.toModel(): Character = Character(
    id = id,
    name = name,
    status = status,
    species = species,
    gender = gender,
    image = image,
    isFavorite = isFavorite
)

fun CharacterDraft.toEntity(): CharacterEntity = CharacterEntity(
    id = 0,
    name = name,
    status = status,
    species = species,
    gender = gender,
    image = image,
    isFavorite = false
)

fun CharacterDto.toEntity(): CharacterEntity = CharacterEntity(
    id = id,
    name = name,
    status = status,
    species = species,
    gender = gender,
    image = image,
    isFavorite = false
)
