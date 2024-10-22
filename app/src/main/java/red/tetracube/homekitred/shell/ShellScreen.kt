package red.tetracube.homekitred.shell

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import red.tetracube.homekitred.app.behaviour.routing.Routes
import red.tetracube.homekitred.iot.addIoTNavigation
import red.tetracube.homekitred.hubcentral.login.LoginScreen
import red.tetracube.homekitred.hubcentral.setup.HubSetupScreen
import red.tetracube.homekitred.hubcentral.setup.HubSetupViewModel
import red.tetracube.homekitred.hubcentral.login.LoginViewModel
import red.tetracube.homekitred.splash.SplashScreen
import red.tetracube.homekitred.splash.SplashViewModel
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
            composable<Routes.Splash> {
                val viewModel: SplashViewModel = viewModel(factory = SplashViewModel.Factory)
                SplashScreen(navController, viewModel)
            }
            composable<Routes.Login> {
                val viewModel: LoginViewModel = viewModel(factory = LoginViewModel.Factory)
                LoginScreen(navController, viewModel)
            }
            composable<Routes.HubSetup> {
                val viewModel: HubSetupViewModel = viewModel(factory = HubSetupViewModel.Factory)
                HubSetupScreen(navController, viewModel)
            }
            addIoTNavigation()
        }
    }
}