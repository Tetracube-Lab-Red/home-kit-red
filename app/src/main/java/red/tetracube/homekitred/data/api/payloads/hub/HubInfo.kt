package red.tetracube.homekitred.data.api.payloads.hub

import kotlinx.serialization.Serializable

@Serializable
data class HubInfo(
    val slug: String,
    val name: String,
    val rooms: List<Room> = emptyList()
)
