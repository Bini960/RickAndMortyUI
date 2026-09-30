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
import plat.rickandmorty.acastrom.ui.locationdetail.LocationDetailRoute
import plat.rickandmorty.acastrom.ui.locationlist.LocationListRoute
import plat.rickandmorty.acastrom.ui.login.LoginScreen
import plat.rickandmorty.acastrom.ui.profile.ProfileRoute

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = LoginDestination
    ) {
        composable<LoginDestination> {
            val activity = LocalActivity.current
            BackHandler {
                activity?.finish()
            }
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

        navigation<LocationsGraph>(startDestination = LocationListDestination) {
            composable<LocationListDestination> {
                LocationListRoute(
                    onLocationClick = { id ->
                        navController.navigate(LocationDetailDestination(id))
                    }
                )
            }

            composable<LocationDetailDestination> { backStackEntry ->
                val destination: LocationDetailDestination = backStackEntry.toRoute()
                LocationDetailRoute(
                    locationId = destination.id,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }

        composable<ProfileDestination> {
            ProfileRoute(
                onLogoutClick = {
                    navController.navigate(LoginDestination) {
                        popUpTo(0)
                    }
                }
            )
        }
    }
}