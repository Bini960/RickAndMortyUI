package plat.rickandmorty.acastrom.navigation

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import plat.rickandmorty.acastrom.ui.characterdetail.CharacterDetailRoute
import plat.rickandmorty.acastrom.ui.characterlist.CharacterListRoute
import plat.rickandmorty.acastrom.ui.components.AppBottomBar
import plat.rickandmorty.acastrom.ui.locationdetail.LocationDetailRoute
import plat.rickandmorty.acastrom.ui.locationlist.LocationListRoute
import plat.rickandmorty.acastrom.ui.login.LoginScreen
import plat.rickandmorty.acastrom.ui.profile.ProfileRoute

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    var isLoggedIn by rememberSaveable { mutableStateOf(false) }
    var selectedTab by rememberSaveable { mutableStateOf(AppTab.Characters) }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (isLoggedIn) {
                AppBottomBar(
                    selectedTab = selectedTab,
                    onTabSelected = { tab ->
                        selectedTab = tab
                        val graphDestination = when (tab) {
                            AppTab.Characters -> CharactersGraph
                            AppTab.Locations -> LocationsGraph
                            AppTab.Profile -> ProfileDestination
                        }
                        navController.navigate(graphDestination) {
                            popUpTo(0) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = LoginDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<LoginDestination> {
                val activity = LocalActivity.current
                BackHandler {
                    activity?.finish()
                }
                LoginScreen(
                    onEmpezarClick = {
                        isLoggedIn = true
                        selectedTab = AppTab.Characters
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
                        isLoggedIn = false
                        navController.navigate(LoginDestination) {
                            popUpTo(0)
                        }
                    }
                )
            }
        }
    }
}