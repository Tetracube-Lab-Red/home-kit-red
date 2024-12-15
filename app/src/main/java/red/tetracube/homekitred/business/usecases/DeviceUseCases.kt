package red.tetracube.homekitred.business.usecases

import red.tetracube.homekitred.business.enumerations.DeviceType
import red.tetracube.homekitred.data.api.datasource.IoTAPIDataSource
import red.tetracube.homekitred.data.api.entities.device.DeviceData
import red.tetracube.homekitred.data.api.entities.device.DeviceProvisioningRequest
import red.tetracube.homekitred.data.api.entities.device.UPSProvisioningFields
import red.tetracube.homekitred.data.db.datasource.DeviceDataSource
import red.tetracube.homekitred.data.db.datasource.HubDataSource
import red.tetracube.homekitred.data.db.entities.DeviceEntity
import red.tetracube.homekitred.models.DeviceProvisioning
import red.tetracube.homekitred.models.UPSProvisioning
import red.tetracube.homekitred.models.errors.HomeKitRedError

class DeviceUseCases(
    private val hubDatasource: HubDataSource,
    private val ioTAPIDataSource: IoTAPIDataSource,
    private val deviceDataSource: DeviceDataSource
) {

    suspend fun sendDeviceProvisioningRequest(
        deviceProvisioningModel: DeviceProvisioning,
        upsProvisioningModel: UPSProvisioning
    ): Result<Unit> {
        val hub = hubDatasource.getActiveHub()!!
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
        deviceDataSource.insert(
            DeviceEntity(
                id = apiDeviceData.id,
                name = apiDeviceData.name,
                apiDeviceData.deviceType,
                hub.id,
                roomId = apiDeviceData.roomId,
            )
        )

        return Result.success(Unit)
    }

}