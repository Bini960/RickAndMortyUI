package plat.rickandmorty.acastrom.ui.startup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import plat.rickandmorty.acastrom.RickAndMortyApplication
import plat.rickandmorty.acastrom.data.SessionRepository
import plat.rickandmorty.acastrom.data.SyncRepository

sealed interface StartupUiState {
    data object Checking : StartupUiState
    data object LoggedIn : StartupUiState
    data object LoggedOut : StartupUiState
}

class StartupViewModel(
    private val sessionRepository: SessionRepository,
    private val syncRepository: SyncRepository
) : ViewModel() {

    var uiState: StartupUiState by mutableStateOf<StartupUiState>(StartupUiState.Checking)
        private set

    init {
        viewModelScope.launch {
            uiState = if (hasValidSession()) StartupUiState.LoggedIn else StartupUiState.LoggedOut
        }
    }

    private suspend fun hasValidSession(): Boolean {
        if (sessionRepository.userName.first() == null) return false
        if (syncRepository.isSynced()) return true
        sessionRepository.clearUserName()
        return false
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as RickAndMortyApplication
                StartupViewModel(
                    sessionRepository = application.container.sessionRepository,
                    syncRepository = application.container.syncRepository
                )
            }
        }
    }
}
