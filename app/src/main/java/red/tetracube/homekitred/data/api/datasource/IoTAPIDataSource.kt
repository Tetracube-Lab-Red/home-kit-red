package red.tetracube.homekitred.data.api.datasource

import io.ktor.client.call.body
import io.ktor.client.plugins.websocket.receiveDeserialized
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.request.get
import io.ktor.client.request.head
import io.ktor.client.request.headers
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.flow.flow
import red.tetracube.homekitred.data.api.entities.device.DeviceData
import red.tetracube.homekitred.data.api.entities.device.DeviceProvisioningRequest
import red.tetracube.homekitred.data.api.entities.device.DeviceRoomJoin
import red.tetracube.homekitred.data.api.entities.device.DeviceTelemetryResponse
import red.tetracube.homekitred.data.api.entities.device.GetDevicesResponse
import java.util.UUID

class IoTAPIDataSource : BaseAPIDataSource() {

    companion object {
        const val BASE_PATH = "/iot"
        const val DEVICES = "/devices"
        const val WEBSOCKET = "/ws"
        const val PROVISIONING = "/telemetry"
        const val ROOM = "/room"
        const val TELEMETRY = "/telemetry"
    }

    suspend fun deviceProvisioning(
        hubURI: String,
        token: String,
        request: DeviceProvisioningRequest
    ): DeviceData =
        client.post("$hubURI$BASE_PATH$DEVICES$PROVISIONING")
        {
            headers {
                append("Authorization", "Bearer $token")
            }
            setBody(request)
        }
            .body()

    suspend fun getDevices(hubURI: String, token: String): GetDevicesResponse =
        client.get("$hubURI$BASE_PATH$DEVICES")
        {
            headers {
                append("Authorization", "Bearer $token")
            }
        }
            .body<GetDevicesResponse>()

    suspend fun getDeviceTelemetry(hubURI: String, token: String, deviceId: UUID) =
        client.head("$hubURI$BASE_PATH$DEVICES/$deviceId$TELEMETRY")
        {
            headers {
                append("Authorization", "Bearer $token")
            }
        }

    fun getDevicesStreaming(websocketURI: String) =
        flow {
            client.webSocket(
                urlString = "$websocketURI$BASE_PATH$DEVICES$WEBSOCKET"
            ) {
                while (true) {
                    val telemetry = receiveDeserialized<DeviceData>()
                    emit(telemetry)
                }
            }
        }

    fun getTelemetryStreaming(websocketURI: String) =
        flow {
            client.webSocket(
                urlString = "$websocketURI$BASE_PATH$DEVICES$TELEMETRY$WEBSOCKET"
            ) {
                while (true) {
                    val telemetry = receiveDeserialized<DeviceTelemetryResponse>()
                    emit(telemetry)
                }
            }
        }

    suspend fun deviceRoomJoin(
        hubURI: String,
        token: String,
        request: DeviceRoomJoin
    ): DeviceRoomJoin =
        client.patch("$hubURI$BASE_PATH$DEVICES$ROOM")
        {
            headers {
                append("Authorization", "Bearer $token")
            }
            setBody(request)
        }
            .body()

}