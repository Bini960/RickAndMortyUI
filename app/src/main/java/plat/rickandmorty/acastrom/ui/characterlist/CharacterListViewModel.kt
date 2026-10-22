package plat.rickandmorty.acastrom.ui.characterlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import plat.rickandmorty.acastrom.RickAndMortyApplication
import plat.rickandmorty.acastrom.data.Character
import plat.rickandmorty.acastrom.data.CharacterRepository
import plat.rickandmorty.acastrom.ui.state.UiAction
import plat.rickandmorty.acastrom.ui.state.UiState

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
            repository.observeCharacters()
                .catch { _uiState.update { state -> state.copy(isLoading = false, hasError = true) } }
                .collect { characters ->
                    _uiState.update { it.copy(isLoading = false, data = characters) }
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
                val application = this[APPLICATION_KEY] as RickAndMortyApplication
                CharacterListViewModel(application.container.characterRepository)
            }
        }
    }
}
