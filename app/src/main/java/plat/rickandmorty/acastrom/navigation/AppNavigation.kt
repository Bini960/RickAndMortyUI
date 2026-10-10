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
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import plat.rickandmorty.acastrom.ui.characterdetail.CharacterDetailRoute
import plat.rickandmorty.acastrom.ui.characterlist.CharacterListRoute
import plat.rickandmorty.acastrom.ui.components.AppBottomBar
import plat.rickandmorty.acastrom.ui.locationdetail.LocationDetailRoute
import plat.rickandmorty.acastrom.ui.locationlist.LocationListRoute
import plat.rickandmorty.acastrom.ui.login.LoginRoute
import plat.rickandmorty.acastrom.ui.profile.ProfileRoute
import plat.rickandmorty.acastrom.ui.startup.StartupRoute

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    var selectedTab by rememberSaveable { mutableStateOf(AppTab.Characters) }

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination
    val showBottomBar = currentDestination?.hierarchy?.any {
        it.hasRoute<CharactersGraph>() ||
                it.hasRoute<LocationsGraph>() ||
                it.hasRoute<ProfileDestination>()
    } == true

    val goToTab: (AppTab) -> Unit = { tab ->
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

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (showBottomBar) {
                AppBottomBar(
                    selectedTab = selectedTab,
                    onTabSelected = goToTab
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = StartupDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<StartupDestination> {
                StartupRoute(
                    onLoggedIn = {
                        selectedTab = AppTab.Characters
                        navController.navigate(CharactersGraph) {
                            popUpTo(StartupDestination) { inclusive = true }
                        }
                    },
                    onLoggedOut = {
                        navController.navigate(LoginDestination) {
                            popUpTo(StartupDestination) { inclusive = true }
                        }
                    }
                )
            }

            composable<LoginDestination> {
                val activity = LocalActivity.current
                BackHandler {
                    activity?.finish()
                }
                LoginRoute(
                    onLoginSuccess = {
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

                composable<CharacterDetailDestination> {
                    CharacterDetailRoute(
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }

            navigation<LocationsGraph>(startDestination = LocationListDestination) {
                composable<LocationListDestination> {
                    BackHandler {
                        goToTab(AppTab.Characters)
                    }
                    LocationListRoute(
                        onLocationClick = { id ->
                            navController.navigate(LocationDetailDestination(id))
                        }
                    )
                }

                composable<LocationDetailDestination> {
                    LocationDetailRoute(
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }

            composable<ProfileDestination> {
                BackHandler {
                    goToTab(AppTab.Characters)
                }
                ProfileRoute(
                    onLoggedOut = {
                        navController.navigate(LoginDestination) {
                            popUpTo(0)
                        }
                        navController.clearBackStack(CharactersGraph)
                        navController.clearBackStack(LocationsGraph)
                    }
                )
            }
        }
    }
}