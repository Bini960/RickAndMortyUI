package plat.rickandmorty.acastrom.navigation

import kotlinx.serialization.Serializable

@Serializable
object LoginDestination

@Serializable
object CharacterListDestination

@Serializable
data class CharacterDetailDestination(val id: Int)