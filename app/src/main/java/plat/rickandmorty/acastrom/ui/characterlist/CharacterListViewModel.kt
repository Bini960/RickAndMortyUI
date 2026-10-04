package plat.rickandmorty.acastrom.ui.characterlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import plat.rickandmorty.acastrom.data.Character
import plat.rickandmorty.acastrom.data.CharacterRepository
import plat.rickandmorty.acastrom.ui.state.UiAction
import plat.rickandmorty.acastrom.ui.state.UiState
import kotlin.coroutines.cancellation.CancellationException

class CharacterListViewModel(
    private val repository: CharacterRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState<List<Character>>(data = emptyList()))
    val uiState: StateFlow<UiState<List<Character>>> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadCharacters()
    }

    fun onAction(action: UiAction) {
        when (action) {
            UiAction.LoadingClick -> onLoadingClick()
            UiAction.RetryClick -> loadCharacters()
        }
    }

    private fun loadCharacters() {
        loadJob?.cancel()
        _uiState.update { it.copy(isLoading = true, hasError = false) }
        loadJob = viewModelScope.launch {
            try {
                val characters = repository.getCharacters()
                _uiState.update { it.copy(isLoading = false, data = characters) }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.update { it.copy(isLoading = false, hasError = true) }
            }
        }
    }

    private fun onLoadingClick() {
        if (!_uiState.value.isLoading) return
        loadJob?.cancel()
        _uiState.update { it.copy(isLoading = false, hasError = true) }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                CharacterListViewModel(CharacterRepository())
            }
        }
    }
}