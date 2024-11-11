package red.tetracube.homekitred.iot

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import red.tetracube.homekitred.app.behaviour.routing.Routes.DeviceProvisioning
import red.tetracube.homekitred.app.behaviour.routing.Routes.IoT
import red.tetracube.homekitred.app.behaviour.routing.Routes.IoTHome
import red.tetracube.homekitred.iot.device.provisioning.DeviceProvisioningScreen
import red.tetracube.homekitred.iot.device.provisioning.DeviceProvisioningViewModel
import red.tetracube.homekitred.iot.home.IoTHomeScreen
import red.tetracube.homekitred.iot.home.IoTHomeViewModel

fun NavGraphBuilder.addIoTNavigation(
    navController: NavHostController
) {
    navigation<IoT>(
        startDestination = IoTHome
    ) {
        composable<IoTHome> {
            val viewModel: IoTHomeViewModel =
                viewModel(factory = IoTHomeViewModel.Companion.Factory)
            IoTHomeScreen(
                viewModel = viewModel,
                navController = navController
            )
        }
        composable<DeviceProvisioning> {
            val viewModel: DeviceProvisioningViewModel =
                viewModel(factory = DeviceProvisioningViewModel.Companion.Factory)
            DeviceProvisioningScreen(
                viewModel = viewModel,
                navHostController = navController
            )
        }
    }
}