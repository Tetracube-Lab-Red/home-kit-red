package red.tetracube.homekitred.data.api.payloads.hub

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class LoginPayloadRequest(
    @JsonNames("name") val name: String,
    @JsonNames("accessCode") val accessCode: String
)