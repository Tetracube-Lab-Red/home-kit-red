package red.tetracube.homekitred.data.api.repositories

import io.ktor.client.call.body
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import red.tetracube.homekitred.data.api.clients.TetraCubeAPIClient

class IoTSenseAPIRepository(
    private val tetraCubeAPIClient: TetraCubeAPIClient
) {

    companion object {
        const val DEVICE_PROVISIONING = "/iot/devices"
    }

    suspend fun deviceProvisioning(
        hubAddress: String,
        token: String,
    ): Result<Void> {
        tetraCubeAPIClient.client.post("$hubAddress$DEVICE_PROVISIONING")
        {
            headers {
                append("Authorization", "Bearer $token")
            }
            setBody(request)
        }
            .body()
        return Result.success(hubBase)
    }

}