package plat.rickandmorty.acastrom.ui.locationdetail

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
import plat.rickandmorty.acastrom.data.Location
import plat.rickandmorty.acastrom.data.LocationRepository
import plat.rickandmorty.acastrom.navigation.LocationDetailDestination
import plat.rickandmorty.acastrom.ui.state.UiAction
import plat.rickandmorty.acastrom.ui.state.UiState
import kotlin.coroutines.cancellation.CancellationException

class LocationDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: LocationRepository
) : ViewModel() {

    private val locationId: Int = savedStateHandle.toRoute<LocationDetailDestination>().id

    private val _uiState = MutableStateFlow(UiState<Location?>(data = null))
    val uiState: StateFlow<UiState<Location?>> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadLocation()
    }

    fun onAction(action: UiAction) {
        when (action) {
            UiAction.LoadingClick -> onLoadingClick()
            UiAction.RetryClick -> loadLocation()
        }
    }

    private fun loadLocation() {
        loadJob?.cancel()
        _uiState.update { it.copy(isLoading = true, hasError = false) }
        loadJob = viewModelScope.launch {
            try {
                val location = repository.getLocationById(locationId)
                _uiState.update {
                    if (location == null) {
                        it.copy(isLoading = false, hasError = true)
                    } else {
                        it.copy(isLoading = false, data = location)
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
                LocationDetailViewModel(
                    savedStateHandle = createSavedStateHandle(),
                    repository = LocationRepository()
                )
            }
        }
    }
}