package red.tetracube.homekitred.business.models.errors

sealed class HomeKitRedError {

    data object UnreachableService : HomeKitRedError()

    data object ModuleNotAvailable : HomeKitRedError()

    data object ServiceError : HomeKitRedError()

    data object Unauthorized : HomeKitRedError()

    data object NotFound : HomeKitRedError()

    data object ClientError : HomeKitRedError()

    data object Conflict : HomeKitRedError()

    data object UnprocessableResult : HomeKitRedError()

    data object GenericError : HomeKitRedError()

}