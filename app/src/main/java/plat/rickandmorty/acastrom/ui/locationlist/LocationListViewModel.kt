package plat.rickandmorty.acastrom.ui.locationlist

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
import plat.rickandmorty.acastrom.data.model.Location
import plat.rickandmorty.acastrom.data.repository.LocationRepository
import plat.rickandmorty.acastrom.ui.state.UiAction
import plat.rickandmorty.acastrom.ui.state.UiState

data class LocationListContent(
    val locations: List<Location> = emptyList(),
    val filter: LocationFilter = LocationFilter.All
)

class LocationListViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val repository: LocationRepository
) : ViewModel() {

    private val filterName: StateFlow<String> =
        savedStateHandle.getStateFlow(FILTER_KEY, LocationFilter.All.name)

    private val _uiState = MutableStateFlow(UiState(data = LocationListContent()))
    val uiState: StateFlow<UiState<LocationListContent>> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadLocations()
    }

    fun onAction(action: UiAction) {
        when (action) {
            UiAction.LoadingClick -> onLoadingClick()
            UiAction.RetryClick -> loadLocations()
        }
    }

    fun onFavoriteClick(location: Location) {
        viewModelScope.launch {
            repository.updateFavorite(location.id, !location.isFavorite)
        }
    }

    fun onFilterSelected(filter: LocationFilter) {
        savedStateHandle[FILTER_KEY] = filter.name
    }

    private fun loadLocations() {
        loadJob?.cancel()
        _uiState.update { it.copy(isLoading = true, hasError = false) }
        loadJob = viewModelScope.launch {
            combine(repository.observeLocations(), filterName) { locations, name ->
                val filter = LocationFilter.fromName(name)
                LocationListContent(
                    locations = filter.apply(locations),
                    filter = filter
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
        private const val FILTER_KEY = "location_filter"

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as RickAndMortyApplication
                LocationListViewModel(
                    savedStateHandle = createSavedStateHandle(),
                    repository = application.container.locationRepository
                )
            }
        }
    }
}
