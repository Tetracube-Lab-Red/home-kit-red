package red.tetracube.homekitred.domain

data class HubConnectionInfo (
    var slug: String = "",
    var active: Boolean = false,
    var token: String = "",
    var apiURI: String = "",
    var websocketURI: String = ""
)