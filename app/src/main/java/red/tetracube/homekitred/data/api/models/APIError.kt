package red.tetracube.homekitred.data.api.models

sealed class APIError {

    data object RemoteUnreachable : APIError()
    data object ServerError : APIError()
    data object Unauthorized : APIError()
    data object ClientError : APIError()
    data object EntityConflicts : APIError()
    data object UnprocessableReply : APIError()
}