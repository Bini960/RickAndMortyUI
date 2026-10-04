package plat.rickandmorty.acastrom.ui.locationlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import plat.rickandmorty.acastrom.data.Location
import plat.rickandmorty.acastrom.ui.components.ErrorLayout
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
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationListScreen(
    uiState: UiState<List<Location>>,
    onAction: (UiAction) -> Unit,
    onLocationClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(uiState.data, key = { it.id }) { location ->
                    LocationRow(
                        location = location,
                        onClick = { onLocationClick(location.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun LocationRow(
    location: Location,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(text = location.name, style = MaterialTheme.typography.titleMedium)
        Text(text = location.type, style = MaterialTheme.typography.bodyMedium)
    }
}

private val previewLocations = listOf(
    Location(1, "Earth (C-137)", "Planet", "Dimension C-137"),
    Location(2, "Abadango", "Cluster", "unknown")
)

@Preview(showBackground = true)
@Composable
private fun LocationListLoadingPreview() {
    RickAndMortyTheme {
        LocationListScreen(
            uiState = UiState(isLoading = true, data = emptyList()),
            onAction = {},
            onLocationClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LocationListErrorPreview() {
    RickAndMortyTheme {
        LocationListScreen(
            uiState = UiState(isLoading = false, data = emptyList(), hasError = true),
            onAction = {},
            onLocationClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LocationListContentPreview() {
    RickAndMortyTheme {
        LocationListScreen(
            uiState = UiState(isLoading = false, data = previewLocations),
            onAction = {},
            onLocationClick = {}
        )
    }
}