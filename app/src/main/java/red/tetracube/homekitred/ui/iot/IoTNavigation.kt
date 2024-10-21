package red.tetracube.homekitred.ui.iot

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import red.tetracube.homekitred.app.behaviour.routing.Routes.IoT
import red.tetracube.homekitred.app.behaviour.routing.Routes.IoTHome
import red.tetracube.homekitred.ui.iot.home.IoTHomeScreen
import red.tetracube.homekitred.ui.iot.home.IoTHomeViewModel

fun NavGraphBuilder.addIoTNavigation() {
    navigation<IoT>(
        startDestination = IoTHome
    ) {
        composable<IoTHome> {
            val viewModel: IoTHomeViewModel = viewModel(factory = IoTHomeViewModel.Factory)
            IoTHomeScreen(viewModel = viewModel)
        }
    }
}