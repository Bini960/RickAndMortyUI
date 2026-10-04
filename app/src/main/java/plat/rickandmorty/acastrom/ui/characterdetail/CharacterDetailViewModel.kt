package plat.rickandmorty.acastrom.ui.characterdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.toRoute
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import plat.rickandmorty.acastrom.data.Character
import plat.rickandmorty.acastrom.data.CharacterRepository
import plat.rickandmorty.acastrom.navigation.CharacterDetailDestination
import plat.rickandmorty.acastrom.ui.state.UiAction
import plat.rickandmorty.acastrom.ui.state.UiState
import kotlin.coroutines.cancellation.CancellationException

class CharacterDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: CharacterRepository
) : ViewModel() {

    private val characterId: Int = savedStateHandle.toRoute<CharacterDetailDestination>().id

    private val _uiState = MutableStateFlow(UiState<Character?>(data = null))
    val uiState: StateFlow<UiState<Character?>> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadCharacter()
    }

    fun onAction(action: UiAction) {
        when (action) {
            UiAction.LoadingClick -> onLoadingClick()
            UiAction.RetryClick -> loadCharacter()
        }
    }

    private fun loadCharacter() {
        loadJob?.cancel()
        _uiState.update { it.copy(isLoading = true, hasError = false) }
        loadJob = viewModelScope.launch {
            try {
                val character = repository.getCharacterById(characterId)
                _uiState.update {
                    if (character == null) {
                        it.copy(isLoading = false, hasError = true)
                    } else {
                        it.copy(isLoading = false, data = character)
                    }
                }
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
                CharacterDetailViewModel(
                    savedStateHandle = createSavedStateHandle(),
                    repository = CharacterRepository()
                )
            }
        }
    }
}