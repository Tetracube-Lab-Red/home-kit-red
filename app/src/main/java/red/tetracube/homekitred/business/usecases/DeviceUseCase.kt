package red.tetracube.homekitred.business.usecases

import red.tetracube.homekitred.business.enumerations.DeviceType
import red.tetracube.homekitred.business.models.errors.HomeKitRedError
import red.tetracube.homekitred.data.api.datasource.IoTAPIDataSource
import red.tetracube.homekitred.data.api.entities.device.DeviceData
import red.tetracube.homekitred.data.api.entities.device.DeviceProvisioningRequest
import red.tetracube.homekitred.data.api.entities.device.UPSProvisioningFields
import red.tetracube.homekitred.data.db.datasource.DeviceDataSource
import red.tetracube.homekitred.data.db.datasource.HubDataSource
import red.tetracube.homekitred.data.db.entities.DeviceEntity
import red.tetracube.homekitred.iot.device.provisioning.models.DeviceProvisioningFormModel
import red.tetracube.homekitred.iot.device.provisioning.models.UPSProvisioningFormModel

class DeviceUseCase(
    private val hubDatasource: HubDataSource,
    private val ioTAPIDataSource: IoTAPIDataSource,
    private val deviceDataSource: DeviceDataSource
) {

    suspend fun sendDeviceProvisioningRequest(
        deviceProvisioningFormModel: DeviceProvisioningFormModel,
        upsProvisioningViewModel: UPSProvisioningFormModel
    ): Result<Unit> {
        val hub = hubDatasource.getActiveHub()!!
        val request = DeviceProvisioningRequest(
            deviceProvisioningFormModel.deviceType.internalType,
            deviceProvisioningFormModel.deviceName.value,
            roomId = null,
            upsProvisioning = if (deviceProvisioningFormModel.deviceType.internalType == DeviceType.UPS) {
                UPSProvisioningFields(
                    deviceAddress = upsProvisioningViewModel.nutServerHost.value,
                    devicePort = upsProvisioningViewModel.nutServerPort.value.toInt(),
                    internalName = upsProvisioningViewModel.nutUPSAlias.value
                )
            } else {
                null
            }
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