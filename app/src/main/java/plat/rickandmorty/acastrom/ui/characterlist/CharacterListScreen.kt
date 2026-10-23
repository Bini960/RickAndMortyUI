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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
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
import plat.rickandmorty.acastrom.ui.components.EmptyLayout
import plat.rickandmorty.acastrom.ui.components.ErrorLayout
import plat.rickandmorty.acastrom.ui.components.FavoriteButton
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
        onFavoriteClick = viewModel::onFavoriteClick,
        onFavoritesFilterClick = viewModel::onFavoritesFilterClick,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterListScreen(
    uiState: UiState<CharacterListContent>,
    onAction: (UiAction) -> Unit,
    onCharacterClick: (Int) -> Unit,
    onFavoriteClick: (Character) -> Unit,
    onFavoritesFilterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val content = uiState.data

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
                    actions = {
                        IconButton(onClick = onFavoritesFilterClick) {
                            Icon(
                                imageVector = if (content.showOnlyFavorites) {
                                    Icons.Filled.Favorite
                                } else {
                                    Icons.Outlined.FavoriteBorder
                                },
                                contentDescription = if (content.showOnlyFavorites) {
                                    "Mostrar todos los personajes"
                                } else {
                                    "Mostrar solo favoritos"
                                }
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        ) { paddingValues ->
            if (content.characters.isEmpty()) {
                EmptyLayout(
                    message = "No hay elementos para mostrar",
                    modifier = Modifier.padding(paddingValues)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    items(content.characters, key = { it.id }) { character ->
                        CharacterRow(
                            character = character,
                            onClick = { onCharacterClick(character.id) },
                            onFavoriteClick = { onFavoriteClick(character) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CharacterRow(
    character: Character,
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
        CharacterImage(
            imageUrl = character.image,
            modifier = Modifier.size(56.dp)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp),
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
        FavoriteButton(
            isFavorite = character.isFavorite,
            onClick = onFavoriteClick,
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

private val previewCharacters = listOf(
    Character(1, "Rick Sanchez", "Alive", "Human", "Male", "https://rickandmortyapi.com/api/character/avatar/1.jpeg", isFavorite = true),
    Character(2, "Morty Smith", "Alive", "Human", "Male", "https://rickandmortyapi.com/api/character/avatar/2.jpeg")
)

@Preview(showBackground = true)
@Composable
private fun CharacterListLoadingPreview() {
    RickAndMortyTheme {
        CharacterListScreen(
            uiState = UiState(isLoading = true, data = CharacterListContent()),
            onAction = {},
            onCharacterClick = {},
            onFavoriteClick = {},
            onFavoritesFilterClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CharacterListErrorPreview() {
    RickAndMortyTheme {
        CharacterListScreen(
            uiState = UiState(isLoading = false, data = CharacterListContent(), hasError = true),
            onAction = {},
            onCharacterClick = {},
            onFavoriteClick = {},
            onFavoritesFilterClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CharacterListContentPreview() {
    RickAndMortyTheme {
        CharacterListScreen(
            uiState = UiState(isLoading = false, data = CharacterListContent(characters = previewCharacters)),
            onAction = {},
            onCharacterClick = {},
            onFavoriteClick = {},
            onFavoritesFilterClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CharacterListEmptyFavoritesPreview() {
    RickAndMortyTheme {
        CharacterListScreen(
            uiState = UiState(isLoading = false, data = CharacterListContent(showOnlyFavorites = true)),
            onAction = {},
            onCharacterClick = {},
            onFavoriteClick = {},
            onFavoritesFilterClick = {}
        )
    }
}
