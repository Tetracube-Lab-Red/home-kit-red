package red.tetracube.homekitred.ui.iot

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.navigation
import androidx.navigation.toRoute
import red.tetracube.homekitred.navigation.Routes.DeviceProvisioning
import red.tetracube.homekitred.navigation.Routes.DeviceRoomJoin
import red.tetracube.homekitred.navigation.Routes.IoT
import red.tetracube.homekitred.navigation.Routes.IoTHome
import red.tetracube.homekitred.ui.iot.device.provisioning.DeviceProvisioningScreen
import red.tetracube.homekitred.ui.iot.device.provisioning.DeviceProvisioningViewModel
import red.tetracube.homekitred.ui.iot.device.room.DeviceRoomDialog
import red.tetracube.homekitred.ui.iot.device.room.DeviceRoomViewModel
import red.tetracube.homekitred.ui.iot.home.IoTHomeScreen
import red.tetracube.homekitred.ui.iot.home.IoTHomeViewModel
import java.util.UUID

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
            val deviceId: DeviceRoomJoin = backStackEntry.toRoute()
            val viewModel: DeviceRoomViewModel = viewModel(
                factory = DeviceRoomViewModel.Factory(
                    UUID.fromString(deviceId.deviceId)
                )
            )
            DeviceRoomDialog(viewModel, navController)
        }
    }
}