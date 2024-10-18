package red.tetracube.homekitred.ui.iot.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import red.tetracube.homekitred.app.behaviour.routing.Routes.IoT
import red.tetracube.homekitred.app.behaviour.routing.Routes.IoTHome
import red.tetracube.homekitred.ui.iot.IoTHomeScreen

fun NavGraphBuilder.addIoTNavigation() {
    navigation<IoT>(
        startDestination = IoTHome
    ) {
        composable<IoTHome> {
            IoTHomeScreen()
        }
    }
}