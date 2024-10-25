package red.tetracube.homekitred.data.api.clients

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.request.accept
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import red.tetracube.homekitred.domain.HomeKitRedError

class TetraCubeAPIClient {

    val client: HttpClient
        get() = HttpClient(CIO) {
            expectSuccess = true

            install(Logging) {
                level = LogLevel.ALL
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 15000L
                connectTimeoutMillis = 15000L
                socketTimeoutMillis = 15000L
            }

            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        prettyPrint = true
                        isLenient = true
                    }
                )
            }

            install(DefaultRequest) {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                accept(ContentType.Application.Json)
            }

            install(WebSockets) {
                pingIntervalMillis = 20_000
            }

            HttpResponseValidator {
                handleResponseExceptionWithRequest { exception, request ->
                    val clientException = exception as? ClientRequestException
                    val serverException = exception as? ServerResponseException
                    val timeoutException = exception as? HttpRequestTimeoutException

                    throw if (clientException != null) {
                        if (clientException.response.status == HttpStatusCode.Conflict) {
                            HomeKitRedError.Conflict
                        } else if (clientException.response.status == HttpStatusCode.Unauthorized) {
                            HomeKitRedError.Unauthorized
                        } else if (clientException.response.status == HttpStatusCode.Forbidden) {
                            HomeKitRedError.Unauthorized
                        } else if (clientException.response.status == HttpStatusCode.NotFound) {
                            HomeKitRedError.NotFound
                        } else {
                            HomeKitRedError.ClientError
                        }
                    } else if (serverException != null) {
                        HomeKitRedError.ServiceError
                    } else if (timeoutException != null) {
                        HomeKitRedError.UnreachableService
                    } else {
                        HomeKitRedError.GenericError
                    }
                }
            }
        }

}