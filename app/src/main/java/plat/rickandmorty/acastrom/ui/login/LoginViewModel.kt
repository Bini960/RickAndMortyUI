package plat.rickandmorty.acastrom.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.launch
import plat.rickandmorty.acastrom.RickAndMortyApplication
import plat.rickandmorty.acastrom.data.SessionRepository
import plat.rickandmorty.acastrom.data.SyncRepository
import kotlin.coroutines.cancellation.CancellationException

private const val MAX_NAME_LENGTH = 30

data class LoginUiState(
    val name: String = "",
    val isLoggingIn: Boolean = false,
    val hasError: Boolean = false,
    val isLoggedIn: Boolean = false
) {
    val canLogin: Boolean
        get() = name.isNotBlank() && !isLoggingIn && !isLoggedIn
}

class LoginViewModel(
    private val sessionRepository: SessionRepository,
    private val syncRepository: SyncRepository
) : ViewModel() {

    var uiState by mutableStateOf(LoginUiState())
        private set

    fun onNameChange(newName: String) {
        uiState = uiState.copy(
            name = newName.take(MAX_NAME_LENGTH),
            hasError = false
        )
    }

    fun onLoginClick() {
        val current = uiState
        if (!current.canLogin) return
        uiState = current.copy(isLoggingIn = true, hasError = false)
        viewModelScope.launch {
            uiState = try {
                syncRepository.syncIfNeeded()
                sessionRepository.saveUserName(current.name)
                uiState.copy(isLoggingIn = false, isLoggedIn = true)
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                uiState.copy(isLoggingIn = false, hasError = true)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as RickAndMortyApplication
                LoginViewModel(
                    sessionRepository = application.container.sessionRepository,
                    syncRepository = application.container.syncRepository
                )
            }
        }
    }
}
