package red.tetracube.homekitred.data.api.entities.hub

import com.fasterxml.jackson.annotation.JsonCreator
import java.util.UUID

data class HubCreateResponse @JsonCreator constructor(
    val id: UUID,
    val name: String
)
