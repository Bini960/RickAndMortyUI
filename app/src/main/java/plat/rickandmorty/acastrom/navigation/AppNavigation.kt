package plat.rickandmorty.acastrom.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import plat.rickandmorty.acastrom.ui.characters.CharacterListRoute
import plat.rickandmorty.acastrom.ui.detail.CharacterDetailRoute
import plat.rickandmorty.acastrom.ui.login.LoginScreen
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = LoginDestination
    ) {
        composable<LoginDestination> {
            LoginScreen(
                onEmpezarClick = {
                    navController.navigate(CharacterListDestination) {
                        popUpTo(LoginDestination) { inclusive = true }
                    }
                }
            )
        }

        composable<CharacterListDestination> {
            val activity = LocalActivity.current
            BackHandler {
                activity?.finish()
            }
            CharacterListRoute(
                onCharacterClick = { id ->
                    navController.navigate(CharacterDetailDestination(id))
                }
            )
        }

        composable<CharacterDetailDestination> { backStackEntry ->
            val destination: CharacterDetailDestination = backStackEntry.toRoute()
            CharacterDetailRoute(
                characterId = destination.id,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}