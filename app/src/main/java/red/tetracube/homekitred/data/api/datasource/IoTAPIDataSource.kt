package red.tetracube.homekitred.data.api.datasource

import io.ktor.client.call.body
import io.ktor.client.plugins.websocket.receiveDeserialized
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.flow.flow
import red.tetracube.homekitred.data.api.entities.device.DeviceData
import red.tetracube.homekitred.data.api.entities.device.DeviceProvisioningRequest
import red.tetracube.homekitred.data.api.entities.device.DeviceTelemetryResponse
import red.tetracube.homekitred.data.api.entities.device.GetDevicesResponse

class IoTAPIDataSource : BaseAPIDataSource() {

    companion object {
        const val BASE_PATH = "/iot"
        const val DEVICES = "/devices"
        const val PROVISIONING = "/telemetry"
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

    suspend fun getDeviceTelemetry(
        hubAddress: String,
        token: String,
        deviceSlug: String
    ): DeviceTelemetryResponse {
        return client.get("$hubAddress$DEVICE_RESOURCES/$deviceSlug$TELEMETRY_RESOURCES")
        {
            headers {
                append("Authorization", "Bearer $token")
            }
        }
            .body<DeviceTelemetryResponse>()
    }

    fun getTelemetryStreaming(streamingHubAddress: String) = flow {
        client.webSocket(
            urlString = "$streamingHubAddress$DEVICE_RESOURCES$TELEMETRY_RESOURCES"
        ) {
            while (true) {
                val telemetry = receiveDeserialized<DeviceTelemetryResponse>()
                emit(telemetry)
            }
        }
    }

}