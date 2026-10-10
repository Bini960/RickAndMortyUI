package plat.rickandmorty.acastrom

import android.app.Application
import plat.rickandmorty.acastrom.dependencyinjection.AppContainer

class RickAndMortyApplication : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}