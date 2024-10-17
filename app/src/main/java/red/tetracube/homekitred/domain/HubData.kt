package red.tetracube.homekitred.domain

data class HubData (
    var slug: String,
    var name: String,
    var token: String,
    var apiURI: String,
    var websocketURI: String
)