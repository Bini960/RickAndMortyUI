package plat.rickandmorty.acastrom.ui.characterform

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import plat.rickandmorty.acastrom.ui.components.ErrorLayout
import plat.rickandmorty.acastrom.ui.theme.RickAndMortyTheme

private val FormMaxWidth = 480.dp

@Composable
fun CharacterFormRoute(
    onBackClick: () -> Unit,
    onCreated: (Int) -> Unit,
    onUpdated: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CharacterFormViewModel = viewModel(factory = CharacterFormViewModel.Factory)
) {
    val uiState = viewModel.uiState

    LaunchedEffect(uiState.savedId) {
        val savedId = uiState.savedId
        if (savedId != null) {
            if (uiState.isEditing) onUpdated() else onCreated(savedId)
        }
    }

    CharacterFormScreen(
        uiState = uiState,
        onNameChange = viewModel::onNameChange,
        onSpeciesChange = viewModel::onSpeciesChange,
        onStatusChange = viewModel::onStatusChange,
        onGenderChange = viewModel::onGenderChange,
        onImageChange = viewModel::onImageChange,
        onSaveClick = viewModel::onSaveClick,
        onRetryLoadClick = viewModel::onRetryLoadClick,
        onBackClick = onBackClick,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterFormScreen(
    uiState: CharacterFormUiState,
    onNameChange: (String) -> Unit,
    onSpeciesChange: (String) -> Unit,
    onStatusChange: (String) -> Unit,
    onGenderChange: (String) -> Unit,
    onImageChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    onRetryLoadClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (uiState.loadFailed) {
        ErrorLayout(
            message = "Error al obtener el personaje.",
            onRetryClick = onRetryLoadClick,
            modifier = modifier
        )
        return
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(if (uiState.isEditing) "Editar personaje" else "Nuevo personaje") },
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
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            FormContent(
                uiState = uiState,
                onNameChange = onNameChange,
                onSpeciesChange = onSpeciesChange,
                onStatusChange = onStatusChange,
                onGenderChange = onGenderChange,
                onImageChange = onImageChange,
                onSaveClick = onSaveClick,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
private fun FormContent(
    uiState: CharacterFormUiState,
    onNameChange: (String) -> Unit,
    onSpeciesChange: (String) -> Unit,
    onStatusChange: (String) -> Unit,
    onGenderChange: (String) -> Unit,
    onImageChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val enabled = !uiState.isSaving

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = FormMaxWidth)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = uiState.name,
                onValueChange = onNameChange,
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled,
                label = { Text("Nombre") },
                singleLine = true,
                isError = uiState.showErrors && !uiState.isNameValid,
                supportingText = requiredMessage(uiState.showErrors && !uiState.isNameValid),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next
                )
            )
            OutlinedTextField(
                value = uiState.species,
                onValueChange = onSpeciesChange,
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled,
                label = { Text("Especie") },
                singleLine = true,
                isError = uiState.showErrors && !uiState.isSpeciesValid,
                supportingText = requiredMessage(uiState.showErrors && !uiState.isSpeciesValid),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next
                )
            )
            OptionDropdown(
                label = "Estado",
                value = uiState.status,
                options = CharacterStatusOptions,
                onOptionSelected = onStatusChange,
                enabled = enabled
            )
            OptionDropdown(
                label = "Género",
                value = uiState.gender,
                options = CharacterGenderOptions,
                onOptionSelected = onGenderChange,
                enabled = enabled
            )
            OutlinedTextField(
                value = uiState.image,
                onValueChange = onImageChange,
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled,
                label = { Text("URL de la imagen") },
                singleLine = true,
                isError = uiState.showErrors && !uiState.isImageValid,
                supportingText = {
                    Text(
                        if (uiState.showErrors && !uiState.isImageValid) {
                            "Debe empezar con http:// o https://"
                        } else {
                            "Opcional"
                        }
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Uri,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { onSaveClick() })
            )
            if (uiState.saveFailed) {
                Text(
                    text = "No se pudo guardar el personaje. Intenta de nuevo.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Button(
                onClick = onSaveClick,
                enabled = enabled,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = LocalContentColor.current,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Guardando…")
                } else {
                    Text("Guardar")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

private fun requiredMessage(show: Boolean): (@Composable () -> Unit)? {
    return if (show) {
        { Text("Campo obligatorio") }
    } else {
        null
    }
}

@Composable
private fun OptionDropdown(
    label: String,
    value: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = null
                )
            },
            singleLine = true
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable(enabled = enabled, role = Role.DropdownList) { expanded = true }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CharacterFormCreatePreview() {
    RickAndMortyTheme {
        CharacterFormScreen(
            uiState = CharacterFormUiState(),
            onNameChange = {},
            onSpeciesChange = {},
            onStatusChange = {},
            onGenderChange = {},
            onImageChange = {},
            onSaveClick = {},
            onRetryLoadClick = {},
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CharacterFormEditPreview() {
    RickAndMortyTheme {
        CharacterFormScreen(
            uiState = CharacterFormUiState(
                isEditing = true,
                name = "Rick Sanchez",
                species = "Human",
                status = "Alive",
                gender = "Male",
                image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
            ),
            onNameChange = {},
            onSpeciesChange = {},
            onStatusChange = {},
            onGenderChange = {},
            onImageChange = {},
            onSaveClick = {},
            onRetryLoadClick = {},
            onBackClick = {}
        )
    }
}
