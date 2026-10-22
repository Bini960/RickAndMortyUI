package plat.rickandmorty.acastrom.ui.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import plat.rickandmorty.acastrom.RickAndMortyApplication
import plat.rickandmorty.acastrom.data.SessionRepository
import plat.rickandmorty.acastrom.data.SyncRepository
import kotlin.coroutines.cancellation.CancellationException

data class ProfileUiState(
    val name: String? = null,
    val isLoggingOut: Boolean = false,
    val hasError: Boolean = false,
    val isLoggedOut: Boolean = false
)

class ProfileViewModel(
    private val sessionRepository: SessionRepository,
    private val syncRepository: SyncRepository
) : ViewModel() {

    var uiState by mutableStateOf(ProfileUiState())
        private set

    init {
        viewModelScope.launch {
            sessionRepository.userName.filterNotNull().collect { name ->
                uiState = uiState.copy(name = name)
            }
        }
    }

    fun onLogoutClick() {
        val current = uiState
        if (current.isLoggingOut || current.isLoggedOut) return
        uiState = current.copy(isLoggingOut = true, hasError = false)
        viewModelScope.launch {
            uiState = try {
                syncRepository.clearAll()
                sessionRepository.clearUserName()
                uiState.copy(isLoggingOut = false, isLoggedOut = true)
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                uiState.copy(isLoggingOut = false, hasError = true)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as RickAndMortyApplication
                ProfileViewModel(
                    sessionRepository = application.container.sessionRepository,
                    syncRepository = application.container.syncRepository
                )
            }
        }
    }
}
