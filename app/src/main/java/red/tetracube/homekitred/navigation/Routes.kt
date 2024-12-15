package red.tetracube.homekitred.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes {

    @Serializable
    data object Splash : Routes()

    @Serializable
    data object Login : Routes()

    @Serializable
    data object HubSetup: Routes()

    @Serializable
    data object IoT : Routes()

    @Serializable
    data object IoTHome: Routes()

    @Serializable
    data object RoomAdd: Routes()

    @Serializable
    data object DeviceProvisioning: Routes()

    @Serializable
    data class DeviceRoomJoin(val deviceId: String): Routes()

}