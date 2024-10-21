package red.tetracube.homekitred.data.api.payloads.hub

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class HubLoginAPI(
    @JsonNames("slug") val slug: String,
    @JsonNames("name") val name: String,
    @JsonNames("token") val token: String
)
