package red.tetracube.homekitred.ui.shell

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import red.tetracube.homekitred.app.behaviour.routing.Routes
import red.tetracube.homekitred.ui.core.dialogs.HomeKitRedErrorDialog
import red.tetracube.homekitred.ui.login.LoginScreen
import red.tetracube.homekitred.ui.login.LoginViewModel
import red.tetracube.homekitred.ui.setup.HubSetupScreen
import red.tetracube.homekitred.ui.setup.HubSetupViewModel
import red.tetracube.homekitred.ui.splash.SplashScreen
import red.tetracube.homekitred.ui.theme.HomeKitRedTheme

@Composable
fun ShellScreen() {
    val navController = rememberNavController()

    ShellUI(
        navController = navController
    )
}

@Composable
fun ShellUI(navController: NavHostController) {
    HomeKitRedTheme {
        NavHost(
            modifier = Modifier,
            startDestination = Routes.Splash,
            navController = navController
        ) {
            dialog<Routes.ErrorDialog> { backStackEntry ->
                val error: String = backStackEntry.toRoute()
                HomeKitRedErrorDialog(error, navController)
            }
            composable<Routes.Splash> {
                SplashScreen(navController)
            }
            composable<Routes.Login> {
                val viewModel: LoginViewModel = viewModel()
                LoginScreen(navController, viewModel)
            }
            composable<Routes.HubSetup> {
                val viewModel: HubSetupViewModel = viewModel(factory = HubSetupViewModel.Factory)
                HubSetupScreen(navController, viewModel)
            }
        }
    }
}