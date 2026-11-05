package plat.rickandmorty.acastrom.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class PagedResponseDto<T>(
    val results: List<T>
)
