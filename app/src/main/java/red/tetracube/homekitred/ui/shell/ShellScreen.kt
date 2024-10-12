package red.tetracube.homekitred.ui.shell

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import red.tetracube.homekitred.app.behaviour.routing.Routes
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
            composable<Routes.Splash> {
                SplashScreen()
            }
        }
    }
}