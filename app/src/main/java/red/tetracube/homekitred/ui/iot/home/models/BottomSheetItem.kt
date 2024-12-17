package red.tetracube.homekitred.ui.iot.home.models

import androidx.navigation.NavHostController
import red.tetracube.homekitred.R
import red.tetracube.homekitred.navigation.Routes

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
): List<BottomSheetItem.GlobalMenuItem> =
    listOf(
        BottomSheetItem.GlobalMenuItem(
            R.drawable.room_preferences_24px,
            "New room"
        ) {
            globalBehavior()
            navController.navigate(Routes.RoomAdd)
        },

        BottomSheetItem.GlobalMenuItem(
            R.drawable.home_iot_device_24px,
            "Add device"
        ) {
            globalBehavior()
            navController.navigate(Routes.DeviceProvisioning)
        }
    )