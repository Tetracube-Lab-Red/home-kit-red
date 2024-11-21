package red.tetracube.homekitred.data.api.clients

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
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
import io.ktor.serialization.jackson.*
import red.tetracube.homekitred.app.exceptions.HomeKitRedError

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
                jackson {
                    configure(SerializationFeature.INDENT_OUTPUT, true)
                    setDefaultPrettyPrinter(DefaultPrettyPrinter().apply {
                        indentArraysWith(DefaultPrettyPrinter.FixedSpaceIndenter.instance)
                    })
                    registerModule(JavaTimeModule())
                }
            }

            install(DefaultRequest) {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                accept(ContentType.Application.Json)
            }

            install(WebSockets) {
                pingIntervalMillis = 20_000
                contentConverter = JacksonWebsocketContentConverter(
                    jacksonObjectMapper().registerModule(JavaTimeModule())
                )
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
                        if (serverException.response.status == HttpStatusCode.NotImplemented) {
                            HomeKitRedError.ModuleNotAvailable
                        } else {
                            HomeKitRedError.ServiceError
                        }
                    } else if (timeoutException != null) {
                        HomeKitRedError.UnreachableService
                    } else {
                        HomeKitRedError.GenericError
                    }
                }
            }
        }

}