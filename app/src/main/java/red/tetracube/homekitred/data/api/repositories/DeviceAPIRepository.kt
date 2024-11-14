package red.tetracube.homekitred.data.api.repositories

import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import red.tetracube.homekitred.data.api.clients.TetraCubeAPIClient
import red.tetracube.homekitred.data.api.payloads.device.DeviceProvisioningRequest

class DeviceAPIRepository(
    private val tetraCubeAPIClient: TetraCubeAPIClient
) {

    companion object {
        const val DEVICE_PROVISIONING = "/iot/devices"
    }

    suspend fun deviceProvisioning(
        hubAddress: String,
        token: String,
        request: DeviceProvisioningRequest
    ) {
        tetraCubeAPIClient.client.post("$hubAddress$DEVICE_PROVISIONING")
        {
            headers {
                append("Authorization", "Bearer $token")
            }
            setBody(request)
        }
    }

}