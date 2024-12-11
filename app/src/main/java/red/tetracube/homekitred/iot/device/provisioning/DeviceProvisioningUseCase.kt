package red.tetracube.homekitred.iot.device.provisioning

import red.tetracube.homekitred.app.exceptions.HomeKitRedError
import red.tetracube.homekitred.data.api.entities.device.DeviceProvisioningRequest
import red.tetracube.homekitred.data.api.entities.device.UPSProvisioningFields
import red.tetracube.homekitred.data.api.datasource.IoTAPIDataSource
import red.tetracube.homekitred.data.db.datasource.HubDatasource
import red.tetracube.homekitred.data.enumerations.DeviceType
import red.tetracube.homekitred.iot.device.provisioning.models.DeviceProvisioningFormModel
import red.tetracube.homekitred.iot.device.provisioning.models.UPSProvisioningFormModel

class DeviceProvisioningUseCase(
    private val hubDatasource: HubDatasource,
    private val ioTAPIDataSource: IoTAPIDataSource
) {

    suspend fun sendDeviceProvisioningRequest(
        deviceProvisioningFormModel: DeviceProvisioningFormModel,
        upsProvisioningViewModel: UPSProvisioningFormModel
    ): Result<Unit> {
        val hub = hubDatasource.getActiveHub()!!
        val request = DeviceProvisioningRequest(
            deviceProvisioningFormModel.deviceType.internalType,
            deviceProvisioningFormModel.deviceName.value,
            roomSlug = null,
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

        try {
            ioTAPIDataSource.deviceProvisioning(
                hub.apiURI,
                hub.token,
                request
            )
        } catch (ex: HomeKitRedError) {
            return Result.failure(ex)
        }

        return Result.success(Unit)
    }

}