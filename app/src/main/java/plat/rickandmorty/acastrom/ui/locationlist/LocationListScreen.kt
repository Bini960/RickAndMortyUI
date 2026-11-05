package plat.rickandmorty.acastrom.ui.locationlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import plat.rickandmorty.acastrom.data.model.Location
import plat.rickandmorty.acastrom.ui.components.EmptyLayout
import plat.rickandmorty.acastrom.ui.components.ErrorLayout
import plat.rickandmorty.acastrom.ui.components.FavoriteButton
import plat.rickandmorty.acastrom.ui.components.LoadingLayout
import plat.rickandmorty.acastrom.ui.state.UiAction
import plat.rickandmorty.acastrom.ui.state.UiState
import plat.rickandmorty.acastrom.ui.theme.RickAndMortyTheme

@Composable
fun LocationListRoute(
    onLocationClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LocationListViewModel = viewModel(factory = LocationListViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LocationListScreen(
        uiState = uiState,
        onAction = viewModel::onAction,
        onLocationClick = onLocationClick,
        onFavoriteClick = viewModel::onFavoriteClick,
        onFilterSelected = viewModel::onFilterSelected,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationListScreen(
    uiState: UiState<LocationListContent>,
    onAction: (UiAction) -> Unit,
    onLocationClick: (Int) -> Unit,
    onFavoriteClick: (Location) -> Unit,
    onFilterSelected: (LocationFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    val content = uiState.data

    when {
        uiState.hasError -> ErrorLayout(
            message = "Error al obtener listado de ubicaciones.",
            onRetryClick = { onAction(UiAction.RetryClick) },
            modifier = modifier
        )

        uiState.isLoading -> LoadingLayout(
            onClick = { onAction(UiAction.LoadingClick) },
            modifier = modifier
        )

        else -> Scaffold(
            modifier = modifier,
            topBar = {
                TopAppBar(
                    title = { Text("Locations") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    LocationFilter.entries.forEach { filter ->
                        FilterChip(
                            selected = content.filter == filter,
                            onClick = { onFilterSelected(filter) },
                            label = { Text(filter.label) }
                        )
                    }
                }
                if (content.locations.isEmpty()) {
                    EmptyLayout(message = emptyMessage(content.filter))
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(content.locations, key = { it.id }) { location ->
                            LocationRow(
                                location = location,
                                onClick = { onLocationClick(location.id) },
                                onFavoriteClick = { onFavoriteClick(location) }
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun emptyMessage(filter: LocationFilter): String = when (filter) {
    LocationFilter.All -> "No hay elementos para mostrar"
    LocationFilter.Favorites -> "No hay ubicaciones favoritas"
    LocationFilter.NotFavorites -> "No hay ubicaciones sin marcar como favoritas"
}

@Composable
private fun LocationRow(
    location: Location,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(start = 16.dp, top = 12.dp, end = 4.dp, bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = location.name, style = MaterialTheme.typography.titleMedium)
            Text(text = location.type, style = MaterialTheme.typography.bodyMedium)
        }
        FavoriteButton(
            isFavorite = location.isFavorite,
            onClick = onFavoriteClick,
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

private val previewLocations = listOf(
    Location(1, "Earth (C-137)", "Planet", "Dimension C-137", isFavorite = true),
    Location(2, "Abadango", "Cluster", "unknown")
)

@Preview(showBackground = true)
@Composable
private fun LocationListLoadingPreview() {
    RickAndMortyTheme {
        LocationListScreen(
            uiState = UiState(isLoading = true, data = LocationListContent()),
            onAction = {},
            onLocationClick = {},
            onFavoriteClick = {},
            onFilterSelected = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LocationListErrorPreview() {
    RickAndMortyTheme {
        LocationListScreen(
            uiState = UiState(isLoading = false, data = LocationListContent(), hasError = true),
            onAction = {},
            onLocationClick = {},
            onFavoriteClick = {},
            onFilterSelected = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LocationListContentPreview() {
    RickAndMortyTheme {
        LocationListScreen(
            uiState = UiState(isLoading = false, data = LocationListContent(locations = previewLocations)),
            onAction = {},
            onLocationClick = {},
            onFavoriteClick = {},
            onFilterSelected = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LocationListEmptyFavoritesPreview() {
    RickAndMortyTheme {
        LocationListScreen(
            uiState = UiState(
                isLoading = false,
                data = LocationListContent(filter = LocationFilter.Favorites)
            ),
            onAction = {},
            onLocationClick = {},
            onFavoriteClick = {},
            onFilterSelected = {}
        )
    }
}
