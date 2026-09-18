package plat.rickandmorty.acastrom.ui.characters

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import plat.rickandmorty.acastrom.data.Character
import plat.rickandmorty.acastrom.data.CharacterDb
import plat.rickandmorty.acastrom.ui.components.CharacterImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterListRoute(
    onCharacterClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val characterDb = CharacterDb()
    val characters = characterDb.getAllCharacters()

    Scaffold(
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
            items(characters, key = { it.id }) { character ->
                CharacterRow(
                    character = character,
                    onClick = { onCharacterClick(character.id) }
                )
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