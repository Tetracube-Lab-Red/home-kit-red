package red.tetracube.homekitred.data.api.datasource

import io.ktor.client.call.body
import io.ktor.client.request.headers
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import red.tetracube.homekitred.data.api.entities.device.DeviceRoomJoin

class DeviceRoomAPIRepository(
    private val baseAPIDataSource: BaseAPIDataSource
) {

    companion object {
        const val DEVICE_ROOM_RESOURCES = "/iot/devices/room"
    }

    suspend fun deviceRoomJoin(
        hubAddress: String,
        token: String,
        request: DeviceRoomJoin
    ): DeviceRoomJoin {
        return baseAPIDataSource.client.patch("$hubAddress$DEVICE_ROOM_RESOURCES")
        {
            headers {
                append("Authorization", "Bearer $token")
            }
            setBody(request)
        }
            .body<DeviceRoomJoin>()
    }

}