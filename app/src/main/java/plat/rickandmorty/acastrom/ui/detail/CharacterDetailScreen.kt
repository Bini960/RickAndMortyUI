package plat.rickandmorty.acastrom.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import plat.rickandmorty.acastrom.data.CharacterDb
import plat.rickandmorty.acastrom.ui.components.CharacterImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailRoute(
    characterId: Int,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val characterDb = CharacterDb()
    val character = characterDb.getCharacterById(characterId)

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Character details") },
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