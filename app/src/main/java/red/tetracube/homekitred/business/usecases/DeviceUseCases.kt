package red.tetracube.homekitred.business.usecases

import kotlinx.coroutines.flow.map
import red.tetracube.homekitred.business.enumerations.DeviceType
import red.tetracube.homekitred.business.mappers.asEntity
import red.tetracube.homekitred.data.api.datasource.IoTAPIDataSource
import red.tetracube.homekitred.data.api.entities.device.DeviceData
import red.tetracube.homekitred.data.api.entities.device.DeviceProvisioningRequest
import red.tetracube.homekitred.data.api.entities.device.DeviceTelemetryResponse.UPSTelemetryData
import red.tetracube.homekitred.data.api.entities.device.UPSProvisioningFields
import red.tetracube.homekitred.data.db.HomeKitRedDatabase
import red.tetracube.homekitred.models.DeviceProvisioning
import red.tetracube.homekitred.models.UPSProvisioning
import red.tetracube.homekitred.models.errors.HomeKitRedError

class DeviceUseCases(
    private val localDataSource: HomeKitRedDatabase,
    private val ioTAPIDataSource: IoTAPIDataSource,
) {

    suspend fun sendDeviceProvisioningRequest(
        deviceProvisioningModel: DeviceProvisioning,
        upsProvisioningModel: UPSProvisioning
    ): Result<Unit> {
        val hub = localDataSource.hubDataSource().getActiveHub()!!
        val request = DeviceProvisioningRequest(
            deviceProvisioningModel.deviceType,
            deviceProvisioningModel.name,
            roomId = null,
            upsProvisioning = if (deviceProvisioningModel.deviceType == DeviceType.UPS)
                UPSProvisioningFields(
                    deviceAddress = upsProvisioningModel.nutServerURI,
                    devicePort = upsProvisioningModel.nutServerPort,
                    internalName = upsProvisioningModel.upsInternalName
                )
            else null
        )

        val apiProvisioningResponse = try {
            ioTAPIDataSource.deviceProvisioning(
                hub.apiURI,
                hub.token,
                request
            )
        } catch (ex: HomeKitRedError) {
            ex
        }

        if (apiProvisioningResponse is HomeKitRedError) {
            return Result.failure(apiProvisioningResponse)
        }

        val apiDeviceData = apiProvisioningResponse as DeviceData
        localDataSource.deviceDataSource().insert(
            apiDeviceData.asEntity(hub.id)
        )

        return Result.success(Unit)
    }

    suspend fun listenDevicesStreams() {
        val hub = localDataSource.hubDataSource().getActiveHub()!!
        ioTAPIDataSource.getDevicesStreaming(hub.websocketURI)
            .map { it.asEntity(hub.id) }
            .collect { deviceEntity ->
                localDataSource.deviceDataSource().insert(deviceEntity)
            }
    }

    suspend fun listenDeviceTelemetryStreams() {
        val hub = localDataSource.hubDataSource().getActiveHub()!!
        ioTAPIDataSource.getTelemetryStreaming(hub.websocketURI)
            .collect {
                if (it is UPSTelemetryData) {
                    var telemetryEntity = it.asEntity()
                    //   upsTelemetryDatasource.insert(telemetryEntity)
                }
            }
    }

}