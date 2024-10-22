package red.tetracube.homekitred.splash.domain.models

data class HubConnectionInfo (
    var slug: String = "",
    var active: Boolean = false,
    var token: String = "",
    var apiURI: String = ""
)