package red.tetracube.homekitred.business.mappers

import red.tetracube.homekitred.data.db.entities.HubEntity
import red.tetracube.homekitred.business.models.ui.state.HubConnectionInfo

fun HubEntity.toConnectInfo() =
    HubConnectionInfo(
        id = this.id,
        active = this.active,
        token = this.token,
        apiURI = this.apiURI
    )