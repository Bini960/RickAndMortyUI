package plat.rickandmorty.acastrom.data.remote.image

interface ImagePrefetcher {

    suspend fun prefetch(urls: List<String>)
}
