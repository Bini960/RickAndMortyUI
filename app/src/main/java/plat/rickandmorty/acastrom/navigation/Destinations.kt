package plat.rickandmorty.acastrom.navigation

import kotlinx.serialization.Serializable

@Serializable
object StartupDestination

@Serializable
object LoginDestination

@Serializable
object CharactersGraph

@Serializable
object CharacterListDestination

@Serializable
data class CharacterDetailDestination(val id: Int)

@Serializable
object LocationsGraph

@Serializable
object LocationListDestination

@Serializable
data class LocationDetailDestination(val id: Int)

@Serializable
object ProfileDestination

enum class AppTab {
    Characters,
    Locations,
    Profile
}