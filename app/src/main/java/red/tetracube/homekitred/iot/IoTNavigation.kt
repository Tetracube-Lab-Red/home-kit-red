package red.tetracube.homekitred.iot

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.navigation
import androidx.navigation.toRoute
import red.tetracube.homekitred.app.Routes.DeviceProvisioning
import red.tetracube.homekitred.app.Routes.DeviceRoomJoin
import red.tetracube.homekitred.app.Routes.IoT
import red.tetracube.homekitred.app.Routes.IoTHome
import red.tetracube.homekitred.iot.device.provisioning.DeviceProvisioningScreen
import red.tetracube.homekitred.iot.device.provisioning.DeviceProvisioningViewModel
import red.tetracube.homekitred.iot.device.room.DeviceRoomDialog
import red.tetracube.homekitred.iot.device.room.DeviceRoomViewModel
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
        dialog<DeviceRoomJoin> { backStackEntry ->
            val deviceSlug: DeviceRoomJoin = backStackEntry.toRoute()
            val viewModel: DeviceRoomViewModel = viewModel(factory = DeviceRoomViewModel.Factory(deviceSlug.deviceSlug))
            DeviceRoomDialog(viewModel, navController)
        }
    }
}