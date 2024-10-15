package red.tetracube.homekitred.domain.mappers

import red.tetracube.homekitred.data.api.models.APIError
import red.tetracube.homekitred.domain.HomeKitRedError

fun APIError.toDomainError(): HomeKitRedError {
    return when(this) {
        APIError.ClientError -> HomeKitRedError.ClientError
        APIError.EntityConflicts -> HomeKitRedError.Conflict
        APIError.GenericAPIError -> HomeKitRedError.GenericError
        APIError.RemoteUnreachable -> HomeKitRedError.UnreachableService
        APIError.ServerError -> HomeKitRedError.ServiceError
        APIError.Unauthorized -> HomeKitRedError.Unauthorized
        APIError.UnprocessableReply -> HomeKitRedError.UnprocessableResult
    }
}