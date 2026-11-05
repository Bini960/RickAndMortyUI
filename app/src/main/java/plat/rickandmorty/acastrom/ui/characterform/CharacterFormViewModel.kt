package plat.rickandmorty.acastrom.ui.characterform

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import plat.rickandmorty.acastrom.RickAndMortyApplication
import plat.rickandmorty.acastrom.data.model.CharacterDraft
import plat.rickandmorty.acastrom.data.repository.CharacterRepository
import plat.rickandmorty.acastrom.navigation.CharacterFormDestination
import plat.rickandmorty.acastrom.navigation.NEW_CHARACTER_ID
import kotlin.coroutines.cancellation.CancellationException

val CharacterStatusOptions = listOf("Alive", "Dead", "unknown")
val CharacterGenderOptions = listOf("Female", "Male", "Genderless", "unknown")

private const val UNKNOWN_VALUE = "unknown"
private const val MAX_TEXT_LENGTH = 50
private const val MAX_IMAGE_LENGTH = 300

data class CharacterFormUiState(
    val isEditing: Boolean = false,
    val isLoading: Boolean = false,
    val loadFailed: Boolean = false,
    val name: String = "",
    val species: String = "",
    val status: String = UNKNOWN_VALUE,
    val gender: String = UNKNOWN_VALUE,
    val image: String = "",
    val showErrors: Boolean = false,
    val isSaving: Boolean = false,
    val saveFailed: Boolean = false,
    val savedId: Int? = null
) {
    val isNameValid: Boolean
        get() = name.isNotBlank()

    val isSpeciesValid: Boolean
        get() = species.isNotBlank()

    val isImageValid: Boolean
        get() {
            val url = image.trim()
            return url.isEmpty() ||
                    url.startsWith("http://", ignoreCase = true) ||
                    url.startsWith("https://", ignoreCase = true)
        }

    val isValid: Boolean
        get() = isNameValid && isSpeciesValid && isImageValid
}

class CharacterFormViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: CharacterRepository
) : ViewModel() {

    private val characterId: Int = savedStateHandle.toRoute<CharacterFormDestination>().id
    private val isEditing: Boolean = characterId != NEW_CHARACTER_ID

    var uiState by mutableStateOf(CharacterFormUiState(isEditing = isEditing, isLoading = isEditing))
        private set

    init {
        if (isEditing) loadCharacter()
    }

    fun onNameChange(value: String) {
        uiState = uiState.copy(name = value.take(MAX_TEXT_LENGTH), saveFailed = false)
    }

    fun onSpeciesChange(value: String) {
        uiState = uiState.copy(species = value.take(MAX_TEXT_LENGTH), saveFailed = false)
    }

    fun onStatusChange(value: String) {
        uiState = uiState.copy(status = value, saveFailed = false)
    }

    fun onGenderChange(value: String) {
        uiState = uiState.copy(gender = value, saveFailed = false)
    }

    fun onImageChange(value: String) {
        uiState = uiState.copy(image = value.take(MAX_IMAGE_LENGTH), saveFailed = false)
    }

    fun onRetryLoadClick() {
        loadCharacter()
    }

    fun onSaveClick() {
        val current = uiState
        if (current.isSaving || current.isLoading || current.savedId != null) return
        if (!current.isValid) {
            uiState = current.copy(showErrors = true)
            return
        }
        uiState = current.copy(isSaving = true, saveFailed = false)
        val draft = CharacterDraft(
            name = current.name.trim(),
            status = current.status,
            species = current.species.trim(),
            gender = current.gender,
            image = current.image.trim()
        )
        viewModelScope.launch {
            uiState = try {
                val savedId = if (isEditing) {
                    repository.updateCharacter(characterId, draft)
                    characterId
                } else {
                    repository.createCharacter(draft)
                }
                uiState.copy(isSaving = false, savedId = savedId)
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                uiState.copy(isSaving = false, saveFailed = true)
            }
        }
    }

    private fun loadCharacter() {
        uiState = uiState.copy(isLoading = true, loadFailed = false)
        viewModelScope.launch {
            uiState = try {
                val character = repository.observeCharacterById(characterId).first()
                if (character == null) {
                    uiState.copy(isLoading = false, loadFailed = true)
                } else {
                    uiState.copy(
                        isLoading = false,
                        name = character.name,
                        species = character.species,
                        status = character.status,
                        gender = character.gender,
                        image = character.image
                    )
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                uiState.copy(isLoading = false, loadFailed = true)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as RickAndMortyApplication
                CharacterFormViewModel(
                    savedStateHandle = createSavedStateHandle(),
                    repository = application.container.characterRepository
                )
            }
        }
    }
}
