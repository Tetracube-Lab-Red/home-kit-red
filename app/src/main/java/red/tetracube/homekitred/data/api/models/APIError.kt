package red.tetracube.homekitred.data.api.models


sealed class APIError : Throwable() {

    data object RemoteUnreachable : APIError() {
        private fun readResolve(): Any = RemoteUnreachable
    }

    data object ServerError : APIError() {
        private fun readResolve(): Any = ServerError
    }

    data object Unauthorized : APIError() {
        private fun readResolve(): Any = Unauthorized
    }

    data object ClientError : APIError() {
        private fun readResolve(): Any = ClientError
    }

    data object EntityConflicts : APIError() {
        private fun readResolve(): Any = EntityConflicts
    }

    data object UnprocessableReply : APIError() {
        private fun readResolve(): Any = UnprocessableReply
    }

    data object GenericAPIError : APIError() {
        private fun readResolve(): Any = GenericAPIError
    }
}