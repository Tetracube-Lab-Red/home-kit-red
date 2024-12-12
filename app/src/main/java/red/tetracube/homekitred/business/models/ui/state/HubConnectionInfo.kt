package red.tetracube.homekitred.business.models.ui.state

import java.util.UUID

data class HubConnectionInfo (
    var id: UUID ,
    var active: Boolean,
    var token: String,
    var apiURI: String
)