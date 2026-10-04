package plat.rickandmorty.acastrom.ui.characterlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
fun CharacterListRoute(
    onCharacterClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CharacterListViewModel = viewModel(factory = CharacterListViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CharacterListScreen(
        uiState = uiState,
        onAction = viewModel::onAction,
        onCharacterClick = onCharacterClick,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterListScreen(
    uiState: UiState<List<Character>>,
    onAction: (UiAction) -> Unit,
    onCharacterClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        uiState.hasError -> ErrorLayout(
            message = "Error al obtener listado de personajes.",
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
                    title = { Text("Characters") },
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
                items(uiState.data, key = { it.id }) { character ->
                    CharacterRow(
                        character = character,
                        onClick = { onCharacterClick(character.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CharacterRow(
    character: Character,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CharacterImage(
            imageUrl = character.image,
            modifier = Modifier.size(56.dp)
        )
        Column(
            modifier = Modifier.padding(start = 16.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = character.name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(text = "${character.species} - ${character.status}")
            Text(
                text = character.gender,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

private val previewCharacters = listOf(
    Character(1, "Rick Sanchez", "Alive", "Human", "Male", "https://rickandmortyapi.com/api/character/avatar/1.jpeg"),
    Character(2, "Morty Smith", "Alive", "Human", "Male", "https://rickandmortyapi.com/api/character/avatar/2.jpeg")
)

@Preview(showBackground = true)
@Composable
private fun CharacterListLoadingPreview() {
    RickAndMortyTheme {
        CharacterListScreen(
            uiState = UiState(isLoading = true, data = emptyList()),
            onAction = {},
            onCharacterClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CharacterListErrorPreview() {
    RickAndMortyTheme {
        CharacterListScreen(
            uiState = UiState(isLoading = false, data = emptyList(), hasError = true),
            onAction = {},
            onCharacterClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CharacterListContentPreview() {
    RickAndMortyTheme {
        CharacterListScreen(
            uiState = UiState(isLoading = false, data = previewCharacters),
            onAction = {},
            onCharacterClick = {}
        )
    }
}