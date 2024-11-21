package red.tetracube.homekitred.data.api.repositories

import io.ktor.client.call.body
import io.ktor.client.plugins.websocket.receiveDeserialized
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.flow.flow
import red.tetracube.homekitred.data.api.clients.TetraCubeAPIClient
import red.tetracube.homekitred.data.api.payloads.device.DeviceProvisioningRequest
import red.tetracube.homekitred.data.api.payloads.device.DeviceTelemetryResponse
import red.tetracube.homekitred.data.api.payloads.device.GetDevicesResponse

class DeviceAPIRepository(
    private val tetraCubeAPIClient: TetraCubeAPIClient
) {

    companion object {
        const val DEVICE_RESOURCES = "/iot/devices"
        const val TELEMETRY_RESOURCES = "/telemetry"
    }

    suspend fun deviceProvisioning(
        hubAddress: String,
        token: String,
        request: DeviceProvisioningRequest
    ) {
        tetraCubeAPIClient.client.post("$hubAddress$DEVICE_RESOURCES")
        {
            headers {
                append("Authorization", "Bearer $token")
            }
            setBody(request)
        }
    }

    suspend fun getDevices(
        hubAddress: String,
        token: String
    ): GetDevicesResponse {
        return tetraCubeAPIClient.client.get("$hubAddress$DEVICE_RESOURCES")
        {
            headers {
                append("Authorization", "Bearer $token")
            }
        }
            .body<GetDevicesResponse>()
    }

    suspend fun getDeviceTelemetry(
        hubAddress: String,
        token: String,
        deviceSlug: String
    ): DeviceTelemetryResponse {
        return tetraCubeAPIClient.client.get("$hubAddress$DEVICE_RESOURCES/$deviceSlug$TELEMETRY_RESOURCES")
        {
            headers {
                append("Authorization", "Bearer $token")
            }
        }
            .body<DeviceTelemetryResponse>()
    }

    fun getTelemetryStreaming(hubAddress: String) = flow {
        tetraCubeAPIClient.client.webSocket(
            urlString = "$hubAddress$DEVICE_RESOURCES$TELEMETRY_RESOURCES"
        ) {
            while(true) {
                val telemetry = receiveDeserialized<DeviceTelemetryResponse>()
                emit(telemetry)
            }
        }
    }

}