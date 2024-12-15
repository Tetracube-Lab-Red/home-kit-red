package red.tetracube.homekitred.models.errors

sealed class HomeKitRedError : Exception() {

    object UnreachableService : HomeKitRedError() {
        private fun readResolve(): Any = UnreachableService
    }

    object ModuleNotAvailable : HomeKitRedError() {
        private fun readResolve(): Any = ModuleNotAvailable
    }

    object ServiceError : HomeKitRedError() {
        private fun readResolve(): Any = ServiceError
    }

    object Unauthorized : HomeKitRedError() {
        private fun readResolve(): Any = Unauthorized
    }

    object NotFound : HomeKitRedError() {
        private fun readResolve(): Any = NotFound
    }

    object ClientError : HomeKitRedError()

    object Conflict : HomeKitRedError() {
        private fun readResolve(): Any = Conflict
    }

    object UnprocessableResult : HomeKitRedError() {
        private fun readResolve(): Any = UnprocessableResult
    }

    object GenericError : HomeKitRedError() {
        private fun readResolve(): Any = GenericError
    }

}