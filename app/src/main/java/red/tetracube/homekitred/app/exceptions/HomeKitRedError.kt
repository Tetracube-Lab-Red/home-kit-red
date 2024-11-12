package red.tetracube.homekitred.app.exceptions

sealed class HomeKitRedError : Throwable() {

    data object UnreachableService : HomeKitRedError() {
        private fun readResolve(): Any = UnreachableService
    }

    data object ModuleNotAvailable : HomeKitRedError() {
        private fun readResolve(): Any = ModuleNotAvailable
    }

    data object ServiceError : HomeKitRedError() {
        private fun readResolve(): Any = ServiceError
    }

    data object Unauthorized : HomeKitRedError() {
        private fun readResolve(): Any = Unauthorized
    }

    data object NotFound : HomeKitRedError() {
        private fun readResolve(): Any = Unauthorized
    }

    data object ClientError : HomeKitRedError() {
        private fun readResolve(): Any = ClientError
    }

    data object Conflict : HomeKitRedError() {
        private fun readResolve(): Any = Conflict
    }

    data object UnprocessableResult : HomeKitRedError() {
        private fun readResolve(): Any = UnprocessableResult
    }

    data object GenericError : HomeKitRedError() {
        private fun readResolve(): Any = GenericError
    }

}