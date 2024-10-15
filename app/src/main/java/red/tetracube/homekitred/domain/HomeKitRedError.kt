package red.tetracube.homekitred.domain


sealed class HomeKitRedError {

    data object RemoteUnreachable : HomeKitRedError()

    data object RemoteError : HomeKitRedError()

    data object Unauthorized : HomeKitRedError()

    data object ClientError : HomeKitRedError()

    data object Conflict : HomeKitRedError()

    data object UnprocessableResult : HomeKitRedError()

}