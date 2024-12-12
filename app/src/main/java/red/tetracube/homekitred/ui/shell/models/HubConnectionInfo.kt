package red.tetracube.homekitred.ui.shell.models

import java.util.UUID

data class HubConnectionInfo (
    var id: UUID ,
    var active: Boolean,
    var token: String,
    var apiURI: String
)