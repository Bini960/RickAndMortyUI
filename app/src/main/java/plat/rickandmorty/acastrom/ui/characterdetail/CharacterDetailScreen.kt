package plat.rickandmorty.acastrom.ui.characterdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import plat.rickandmorty.acastrom.data.Character
import plat.rickandmorty.acastrom.ui.components.CharacterImage
import plat.rickandmorty.acastrom.ui.components.ErrorLayout
import plat.rickandmorty.acastrom.ui.components.LoadingLayout
import plat.rickandmorty.acastrom.ui.state.UiAction
import plat.rickandmorty.acastrom.ui.state.UiState
import plat.rickandmorty.acastrom.ui.theme.RickAndMortyTheme

@Composable
fun CharacterDetailRoute(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CharacterDetailViewModel = viewModel(factory = CharacterDetailViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CharacterDetailScreen(
        uiState = uiState,
        onAction = viewModel::onAction,
        onBackClick = onBackClick,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailScreen(
    uiState: UiState<Character?>,
    onAction: (UiAction) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val character = uiState.data

    when {
        uiState.hasError -> ErrorLayout(
            message = "Error al obtener detalle de personaje.",
            onRetryClick = { onAction(UiAction.RetryClick) },
            modifier = modifier
        )

        uiState.isLoading -> LoadingLayout(
            onClick = { onAction(UiAction.LoadingClick) },
            modifier = modifier
        )

        character != null -> Scaffold(
            modifier = modifier,
            topBar = {
                TopAppBar(
                    title = { Text("Characters details") },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CharacterImage(
                    imageUrl = character.image,
                    modifier = Modifier.size(160.dp)
                )
                Text(
                    text = character.name,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(top = 16.dp, bottom = 24.dp)
                )
                HorizontalDivider()
                DetailRow(label = "Species", value = character.species)
                HorizontalDivider()
                DetailRow(label = "Status", value = character.status)
                HorizontalDivider()
                DetailRow(label = "Gender", value = character.gender)
                HorizontalDivider()
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
    }
}

private val previewCharacter = Character(
    id = 1,
    name = "Rick Sanchez",
    status = "Alive",
    species = "Human",
    gender = "Male",
    image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
)

@Preview(showBackground = true)
@Composable
private fun CharacterDetailLoadingPreview() {
    RickAndMortyTheme {
        CharacterDetailScreen(
            uiState = UiState(isLoading = true, data = null),
            onAction = {},
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CharacterDetailErrorPreview() {
    RickAndMortyTheme {
        CharacterDetailScreen(
            uiState = UiState(isLoading = false, data = null, hasError = true),
            onAction = {},
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CharacterDetailContentPreview() {
    RickAndMortyTheme {
        CharacterDetailScreen(
            uiState = UiState(isLoading = false, data = previewCharacter),
            onAction = {},
            onBackClick = {}
        )
    }
}