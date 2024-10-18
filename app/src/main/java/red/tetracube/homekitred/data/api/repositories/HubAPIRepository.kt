package red.tetracube.homekitred.data.api.repositories

import io.ktor.client.call.body
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.SerializationException
import red.tetracube.homekitred.data.api.clients.TetraCubeAPIClient
import red.tetracube.homekitred.data.api.models.APIError
import red.tetracube.homekitred.data.api.payloads.hub.HubCreateRequest
import red.tetracube.homekitred.data.api.payloads.hub.HubDataAPI
import red.tetracube.homekitred.data.api.payloads.hub.HubInfo
import red.tetracube.homekitred.data.api.payloads.hub.LoginPayloadRequest

class HubAPIRepository(
    private val tetraCubeAPIClient: TetraCubeAPIClient
) {

    companion object {
        const val CREATE_HUB = "/hub"
        const val GET_HUB_INFO_URL = "/hub/info"
        const val HUB_AUTH_URL = "/hub/auth/login"
    }

    suspend fun createHub(hubAddress: String, name: String, password: String): Result<HubInfo> {
        val request = HubCreateRequest(name, password)
        try {
            val hubInfo = tetraCubeAPIClient.client.post("$hubAddress$CREATE_HUB")
            {
                setBody(request)
            }
                .body<HubInfo>()
            return Result.success(hubInfo)
        } catch (clientException: ClientRequestException) {
            return if (clientException.response.status == HttpStatusCode.Conflict) {
                Result.failure(APIError.EntityConflicts)
            } else {
                Result.failure(APIError.ClientError)
            }
        } catch (_: ServerResponseException) {
            return Result.failure(APIError.ServerError)
        } catch (_: SerializationException) {
            return Result.failure(APIError.UnprocessableReply)
        } catch (_: ConnectTimeoutException) {
            return Result.failure(APIError.RemoteUnreachable)
        }  catch (_: HttpRequestTimeoutException) {
            return Result.failure(APIError.RemoteUnreachable)
        } catch (_: Exception) {
            return Result.failure(APIError.GenericAPIError)
        }
    }

    suspend fun hubLogin(hubAddress: String, name: String, password: String): Result<HubDataAPI> {
        val request = LoginPayloadRequest(name, password)
        try {
            val loginReply = tetraCubeAPIClient.client.post("$hubAddress$HUB_AUTH_URL")
            {
                setBody(request)
            }
                .body<HubDataAPI>()
            return Result.success(loginReply)
        } catch (clientException: ClientRequestException) {
            return if (clientException.response.status == HttpStatusCode.Unauthorized) {
                Result.failure(APIError.Unauthorized)
            } else {
                Result.failure(APIError.ClientError)
            }
        } catch (_: ServerResponseException) {
            return Result.failure(APIError.ServerError)
        } catch (_: SerializationException) {
            return Result.failure(APIError.UnprocessableReply)
        } catch (_: ConnectTimeoutException) {
            return Result.failure(APIError.RemoteUnreachable)
        }  catch (_: HttpRequestTimeoutException) {
            return Result.failure(APIError.RemoteUnreachable)
        } catch (_: Exception) {
            return Result.failure(APIError.GenericAPIError)
        }
    }

    suspend fun getHubInfo(hubAddress: String, authToken: String) {
        val requestResult =
            tetraCubeAPIClient.client.get("$hubAddress$GET_HUB_INFO_URL")
            {
                headers {
                    append("Authorization", "Bearer $authToken")
                }
            }
        //.body() as HubInfo
        /* } catch (clientException: ClientRequestException) {
             APIDatasourceErrors.Unauthorized
         } catch (serverException: ServerResponseException) {
             APIDatasourceErrors.ServerError
         } catch (connectionException: Exception) {
             APIDatasourceErrors.RemoteUnreachable
         }

         return if (requestResult is APIDatasourceErrors) {
             Result.failure(requestResult)
         } else {
             Result.success(requestResult as HubInfo)
         }*/
    }

}