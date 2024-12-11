package red.tetracube.homekitred.iot.home.domain.models

import androidx.navigation.NavHostController
import red.tetracube.homekitred.R
import red.tetracube.homekitred.app.Routes
import red.tetracube.homekitred.app.Routes.DeviceRoomJoin
import red.tetracube.homekitred.iot.home.domain.models.BottomSheetItem.DeviceMenuItem
import red.tetracube.homekitred.iot.home.domain.models.BottomSheetItem.GlobalMenuItem

sealed class BottomSheetItem(
    val icon: Int,
    val text: String
) {

    data class GlobalMenuItem(
        val menuIcon: Int,
        val menuText: String,
        val onClick: () -> Unit
    ) : BottomSheetItem(menuIcon, menuText)

    data class DeviceMenuItem(
        val menuIcon: Int,
        val menuText: String,
        val onClick: () -> Unit
    ) : BottomSheetItem(menuIcon, menuText)

}

fun globalMenuItems(
    navController: NavHostController,
    globalBehavior: () -> Unit
): List<GlobalMenuItem> =
    listOf(
        GlobalMenuItem(
            R.drawable.room_preferences_24px,
            "New room"
        ) {
            globalBehavior()
            navController.navigate(Routes.RoomAdd)
        },

        GlobalMenuItem(
            R.drawable.home_iot_device_24px,
            "Add device"
        ) {
            globalBehavior()
            navController.navigate(Routes.DeviceProvisioning)
        }
    )

fun deviceMenuItems(
    navController: NavHostController,
    deviceSlug: String,
    globalBehavior: () -> Unit
): List<DeviceMenuItem> =
    listOf(
        DeviceMenuItem(
            R.drawable.room_preferences_24px,
            "Set device room"
        ) {
            globalBehavior()
            navController.navigate(DeviceRoomJoin(deviceSlug))
        }
    )