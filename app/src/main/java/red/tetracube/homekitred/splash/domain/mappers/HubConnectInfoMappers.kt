package red.tetracube.homekitred.splash.domain.mappers

import red.tetracube.homekitred.data.db.entities.HubEntity
import red.tetracube.homekitred.splash.domain.models.HubConnectionInfo

fun HubEntity.toConnectInfo() =
    HubConnectionInfo(
        slug = this.slug,
        active = this.active,
        token = this.token,
        apiURI = this.apiURI
    )