package plat.rickandmorty.acastrom.navigation

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import plat.rickandmorty.acastrom.ui.characterdetail.CharacterDetailRoute
import plat.rickandmorty.acastrom.ui.characterlist.CharacterListRoute
import plat.rickandmorty.acastrom.ui.login.LoginScreen

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
                    navController.navigate(CharactersGraph) {
                        popUpTo(LoginDestination) { inclusive = true }
                    }
                }
            )
        }

        navigation<CharactersGraph>(startDestination = CharacterListDestination) {
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
}