package plat.rickandmorty.acastrom.ui.characterlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import plat.rickandmorty.acastrom.RickAndMortyApplication
import plat.rickandmorty.acastrom.data.Character
import plat.rickandmorty.acastrom.data.CharacterRepository
import plat.rickandmorty.acastrom.ui.state.UiAction
import plat.rickandmorty.acastrom.ui.state.UiState

data class CharacterListContent(
    val characters: List<Character> = emptyList(),
    val showOnlyFavorites: Boolean = false
)

class CharacterListViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val repository: CharacterRepository
) : ViewModel() {

    private val showOnlyFavorites: StateFlow<Boolean> =
        savedStateHandle.getStateFlow(SHOW_ONLY_FAVORITES_KEY, false)

    private val _uiState = MutableStateFlow(UiState(data = CharacterListContent()))
    val uiState: StateFlow<UiState<CharacterListContent>> = _uiState.asStateFlow()

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

    fun onFavoriteClick(character: Character) {
        viewModelScope.launch {
            repository.updateFavorite(character.id, !character.isFavorite)
        }
    }

    fun onFavoritesFilterClick() {
        savedStateHandle[SHOW_ONLY_FAVORITES_KEY] = !showOnlyFavorites.value
    }

    private fun loadCharacters() {
        loadJob?.cancel()
        _uiState.update { it.copy(isLoading = true, hasError = false) }
        loadJob = viewModelScope.launch {
            combine(repository.observeCharacters(), showOnlyFavorites) { characters, onlyFavorites ->
                CharacterListContent(
                    characters = if (onlyFavorites) characters.filter { it.isFavorite } else characters,
                    showOnlyFavorites = onlyFavorites
                )
            }
                .catch { _uiState.update { state -> state.copy(isLoading = false, hasError = true) } }
                .collect { content ->
                    _uiState.update { it.copy(isLoading = false, data = content) }
                }
        }
    }

    private fun onLoadingClick() {
        if (!_uiState.value.isLoading) return
        loadJob?.cancel()
        _uiState.update { it.copy(isLoading = false, hasError = true) }
    }

    companion object {
        private const val SHOW_ONLY_FAVORITES_KEY = "show_only_favorites"

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as RickAndMortyApplication
                CharacterListViewModel(
                    savedStateHandle = createSavedStateHandle(),
                    repository = application.container.characterRepository
                )
            }
        }
    }
}
