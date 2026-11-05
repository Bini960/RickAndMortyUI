package plat.rickandmorty.acastrom.data.remote.image

import android.content.Context
import coil3.SingletonImageLoader
import coil3.request.ImageRequest
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class CoilImagePrefetcher(
    private val context: Context
) : ImagePrefetcher {

    override suspend fun prefetch(urls: List<String>) {
        val imageLoader = SingletonImageLoader.get(context)
        coroutineScope {
            urls.map { url ->
                async { imageLoader.execute(ImageRequest.Builder(context).data(url).build()) }
            }.awaitAll()
        }
    }
}
