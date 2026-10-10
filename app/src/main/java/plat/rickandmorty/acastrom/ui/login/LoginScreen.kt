package plat.rickandmorty.acastrom.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import plat.rickandmorty.acastrom.R
import plat.rickandmorty.acastrom.ui.theme.RickAndMortyTheme

private val FormMaxWidth = 400.dp

@Composable
fun LoginRoute(
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = viewModel(factory = LoginViewModel.Factory)
) {
    val uiState = viewModel.uiState

    LaunchedEffect(uiState.isLoggedIn) {
        if (uiState.isLoggedIn) onLoginSuccess()
    }

    LoginScreen(
        uiState = uiState,
        onNameChange = viewModel::onNameChange,
        onLoginClick = viewModel::onLoginClick,
        modifier = modifier
    )
}

@Composable
fun LoginScreen(
    uiState: LoginUiState,
    onNameChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.rickandmorty),
            contentDescription = null,
            modifier = Modifier.height(140.dp)
        )
        OutlinedTextField(
            value = uiState.name,
            onValueChange = onNameChange,
            modifier = Modifier
                .padding(top = 32.dp)
                .widthIn(max = FormMaxWidth)
                .fillMaxWidth(),
            enabled = !uiState.isSaving,
            label = { Text("Nombre") },
            singleLine = true,
            isError = uiState.hasError,
            supportingText = if (uiState.hasError) {
                { Text("No se pudo guardar el nombre. Intenta de nuevo.") }
            } else {
                null
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { onLoginClick() })
        )
        Button(
            onClick = onLoginClick,
            enabled = uiState.canLogin,
            modifier = Modifier
                .padding(top = 16.dp)
                .widthIn(max = FormMaxWidth)
                .fillMaxWidth()
        ) {
            Text("Iniciar sesión")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    RickAndMortyTheme {
        LoginScreen(
            uiState = LoginUiState(name = "Ana"),
            onNameChange = {},
            onLoginClick = {}
        )
    }
}