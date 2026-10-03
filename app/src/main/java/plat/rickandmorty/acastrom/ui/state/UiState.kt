package plat.rickandmorty.acastrom.ui.state

data class UiState<T>(
    val isLoading: Boolean = true,
    val data: T,
    val hasError: Boolean = false
)