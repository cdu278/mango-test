package cdu278.mangotest.ui.main

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import cdu278.mangotest.image.ImagePicker
import cdu278.mangotest.ui.main.chats.ChatsScreen
import cdu278.mangotest.ui.main.profile.ProfileScreen

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    goToAuth: () -> Unit,
    imagePicker: ImagePicker,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(null) {
        viewModel.authorizedFlow.collect { authorized ->
            if (!authorized) {
                goToAuth()
            }
        }
    }
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            NavigationBar {
                val backStackEntry by navController.currentBackStackEntryAsState()
                val destination = backStackEntry?.destination
                MainTabs.forEach { tab ->
                    NavigationBarItem(
                        label = { Text(stringResource(tab.title)) },
                        icon = { Icon(painterResource(tab.icon), contentDescription = null) },
                        selected = destination?.hierarchy
                            ?.any { it.hasRoute(tab.route::class) } == true,
                        onClick = {
                            navController.navigate(tab.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                    )
                }
            }
        },
        modifier = modifier
    ) { paddings ->
        NavHost(
            navController,
            startDestination = MainDestinations.Chats,
            modifier = Modifier
                .padding(paddings)
                .consumeWindowInsets(paddings)
        ) {
            composable<MainDestinations.Chats> {
                ChatsScreen(
                    viewModel = hiltViewModel(),
                )
            }
            composable<MainDestinations.Profile> {
                ProfileScreen(
                    viewModel = hiltViewModel(),
                    imagePicker,
                )
            }
        }
    }
}