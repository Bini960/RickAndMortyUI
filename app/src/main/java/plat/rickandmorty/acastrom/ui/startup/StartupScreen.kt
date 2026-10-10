package plat.rickandmorty.acastrom.ui.startup

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import plat.rickandmorty.acastrom.ui.theme.RickAndMortyTheme

@Composable
fun StartupRoute(
    onLoggedIn: () -> Unit,
    onLoggedOut: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: StartupViewModel = viewModel(factory = StartupViewModel.Factory)
) {
    val uiState = viewModel.uiState

    LaunchedEffect(uiState) {
        when (uiState) {
            StartupUiState.Checking -> Unit
            StartupUiState.LoggedIn -> onLoggedIn()
            StartupUiState.LoggedOut -> onLoggedOut()
        }
    }

    StartupScreen(modifier = modifier)
}

@Composable
fun StartupScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Preview(showBackground = true)
@Composable
private fun StartupScreenPreview() {
    RickAndMortyTheme {
        StartupScreen()
    }
}