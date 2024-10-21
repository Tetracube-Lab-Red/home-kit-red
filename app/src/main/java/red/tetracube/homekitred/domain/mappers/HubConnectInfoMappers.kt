package red.tetracube.homekitred.domain.mappers

import red.tetracube.homekitred.data.db.entities.HubEntity
import red.tetracube.homekitred.domain.HubConnectionInfo

fun HubEntity.toConnectInfo() =
    HubConnectionInfo(
        slug = this.slug,
        active = this.active,
        token = this.token,
        apiURI = this.apiURI,
        websocketURI = this.websocketURI
    )