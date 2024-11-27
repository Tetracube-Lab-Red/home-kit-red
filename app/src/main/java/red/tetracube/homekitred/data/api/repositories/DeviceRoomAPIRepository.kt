package red.tetracube.homekitred.data.api.repositories

import io.ktor.client.call.body
import io.ktor.client.request.headers
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import red.tetracube.homekitred.data.api.clients.TetraCubeAPIClient
import red.tetracube.homekitred.data.api.payloads.device.DeviceRoomJoin

class DeviceRoomAPIRepository(
    private val tetraCubeAPIClient: TetraCubeAPIClient
) {

    companion object {
        const val DEVICE_ROOM_RESOURCES = "/iot/devices/room"
    }

    suspend fun deviceRoomJoin(
        hubAddress: String,
        token: String,
        request: DeviceRoomJoin
    ): DeviceRoomJoin {
        return tetraCubeAPIClient.client.patch("$hubAddress$DEVICE_ROOM_RESOURCES")
        {
            headers {
                append("Authorization", "Bearer $token")
            }
            setBody(request)
        }
            .body<DeviceRoomJoin>()
    }

}