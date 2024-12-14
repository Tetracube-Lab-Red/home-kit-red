package red.tetracube.homekitred.ui.shell

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import red.tetracube.homekitred.navigation.Routes
import red.tetracube.homekitred.ui.iot.addIoTNavigation
import red.tetracube.homekitred.ui.hub.login.LoginScreen
import red.tetracube.homekitred.ui.hub.setup.HubSetupScreen
import red.tetracube.homekitred.ui.hub.setup.HubSetupViewModel
import red.tetracube.homekitred.ui.hub.login.LoginViewModel
import red.tetracube.homekitred.ui.hub.room.create.RoomCreateViewModel
import red.tetracube.homekitred.ui.splash.SplashScreen
import red.tetracube.homekitred.ui.splash.SplashViewModel
import red.tetracube.homekitred.ui.theme.HomeKitRedTheme
import red.tetracube.homekitred.ui.hub.room.create.RoomCreateDialog

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
            dialog<Routes.RoomAdd> {
                val viewModel: RoomCreateViewModel =
                    viewModel(factory = RoomCreateViewModel.Factory)
                RoomCreateDialog(navController, viewModel)
            }
            addIoTNavigation(navController = navController)
        }
    }
}