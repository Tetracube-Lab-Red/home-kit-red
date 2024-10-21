package red.tetracube.homekitred.data.api.payloads.hub

import kotlinx.serialization.Serializable

@Serializable
data class HubDetailsAPI(
    val slug: String,
    val name: String,
    val rooms: List<RoomAPI>
)
