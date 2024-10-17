package red.tetracube.homekitred.data.api.payloads.hub

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class LoginPayloadReply(
    @JsonNames("token") val token: String
)
