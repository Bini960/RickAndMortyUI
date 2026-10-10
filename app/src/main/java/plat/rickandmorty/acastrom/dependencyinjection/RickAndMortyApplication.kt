package plat.rickandmorty.acastrom.dependencyinjection

import android.app.Application

class RickAndMortyApplication : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}