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

sealed interface StartupUiState {
    data object Checking : StartupUiState
    data object LoggedIn : StartupUiState
    data object LoggedOut : StartupUiState
}

class StartupViewModel(
    sessionRepository: SessionRepository
) : ViewModel() {

    var uiState: StartupUiState by mutableStateOf<StartupUiState>(StartupUiState.Checking)
        private set

    init {
        viewModelScope.launch {
            val name = sessionRepository.userName.first()
            uiState = if (name == null) StartupUiState.LoggedOut else StartupUiState.LoggedIn
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as RickAndMortyApplication
                StartupViewModel(application.container.sessionRepository)
            }
        }
    }
}