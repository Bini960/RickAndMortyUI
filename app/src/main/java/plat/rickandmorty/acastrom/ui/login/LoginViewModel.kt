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
import java.io.IOException

private const val MAX_NAME_LENGTH = 30

data class LoginUiState(
    val name: String = "",
    val isSaving: Boolean = false,
    val hasError: Boolean = false,
    val isLoggedIn: Boolean = false
) {
    val canLogin: Boolean
        get() = name.isNotBlank() && !isSaving && !isLoggedIn
}

class LoginViewModel(
    private val sessionRepository: SessionRepository
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
        uiState = current.copy(isSaving = true, hasError = false)
        viewModelScope.launch {
            uiState = try {
                sessionRepository.saveUserName(current.name)
                uiState.copy(isSaving = false, isLoggedIn = true)
            } catch (exception: IOException) {
                uiState.copy(isSaving = false, hasError = true)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as RickAndMortyApplication
                LoginViewModel(application.container.sessionRepository)
            }
        }
    }
}