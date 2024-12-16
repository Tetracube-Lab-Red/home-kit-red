package red.tetracube.homekitred.iot.home

import red.tetracube.homekitred.data.db.HomeKitRedDatabase
import red.tetracube.homekitred.data.db.datasource.HubDataSource
import red.tetracube.homekitred.business.usecases.DeviceUseCases

class IoTHomeUseCases(
    private val hubDatasource: HubDataSource,
    private val deviceUseCases: DeviceUseCases,
    private val database: HomeKitRedDatabase
) {

   /* s

    suspend fun listenAPITelemetrySteaming() {
        val hub = hubDatasource.getActiveHub()!!
       // deviceService.listenDeviceTelemetryStreams(hub.websocketURI, hub.token)
    }

    suspend fun getLatestTelemetry(deviceSlug: String): BasicTelemetry {
        return database.upsTelemetryDatasource().getLatestTelemetry(deviceSlug).asBasicTelemetry()
    }

    fun listenDatabaseTelemetryStreaming(): Flow<List<BasicTelemetry>> {
        var upsTelemetryFlow = database.upsTelemetryDatasource().getLatestTelemetriesStream()
            .map { telemetries ->
                telemetries.map { telemetry ->
                    telemetry.asBasicTelemetry()
                }
            }
        return merge(upsTelemetryFlow)
    }

   */

}