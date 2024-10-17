package red.tetracube.homekitred.domain.mappers

import red.tetracube.homekitred.data.db.entities.HubEntity
import red.tetracube.homekitred.domain.HubData

fun HubData.toEntity(isActive: Boolean) =
    HubEntity(
        id = null,
        slug = this.slug,
        name = this.name,
        active = isActive,
        token = this.token,
        apiURI = this.apiURI,
        websocketURI = this.websocketURI
    )