package plat.rickandmorty.acastrom.ui.locationlist

import plat.rickandmorty.acastrom.data.model.Location

enum class LocationFilter(val label: String) {
    All("Todas"),
    Favorites("Favoritas"),
    NotFavorites("No favoritas");

    fun apply(locations: List<Location>): List<Location> = when (this) {
        All -> locations
        Favorites -> locations.filter { it.isFavorite }
        NotFavorites -> locations.filterNot { it.isFavorite }
    }

    companion object {
        fun fromName(name: String?): LocationFilter {
            return entries.firstOrNull { it.name == name } ?: All
        }
    }
}
