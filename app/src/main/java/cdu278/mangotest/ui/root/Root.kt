package cdu278.mangotest.ui.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import cdu278.mangotest.image.ImagePicker
import cdu278.mangotest.ui.auth.signin.SignInScreen
import cdu278.mangotest.ui.auth.signup.SignUpScreen
import cdu278.mangotest.ui.main.MainScreen
import cdu278.mangotest.ui.main.chats.chat.ChatScreen

@Composable
fun Root(
    imagePicker: ImagePicker,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    NavHost(
        navController,
        graph = remember(navController) {
            navController.createGraph(startDestination = Destinations.Main) {
                composable<Destinations.Main> {
                    MainScreen(
                        viewModel = hiltViewModel(),
                        goToAuth = {
                            navController.navigate(Destinations.Auth) {
                                popUpTo(Destinations.Main) { inclusive = true }
                            }
                        },
                        goToChat = { id ->
                            navController.navigate(Destinations.Chat(id))
                        },
                        imagePicker,
                    )
                }
                composable<Destinations.Chat> {
                    ChatScreen(
                        viewModel = hiltViewModel(),
                        goBack = navController::navigateUp,
                    )
                }
                navigation<Destinations.Auth>(startDestination = Destinations.Auth.SignIn) {
                    composable<Destinations.Auth.SignIn> {
                        SignInScreen(
                            viewModel = hiltViewModel(),
                            goToMain = {
                                navController.navigate(Destinations.Main) {
                                    popUpTo(Destinations.Auth) { inclusive = true }
                                }
                            },
                            goToSignUp = {
                                navController.navigate(Destinations.Auth.SignUp)
                            },
                        )
                    }
                    composable<Destinations.Auth.SignUp> {
                        SignUpScreen(
                            viewModel = hiltViewModel(),
                            goToMain = {
                                navController.navigate(Destinations.Main) {
                                    popUpTo(Destinations.Auth) { inclusive = true }
                                }
                            },
                            goBack = navController::navigateUp,
                        )
                    }
                }
            }
        },
        modifier = modifier
    )
}