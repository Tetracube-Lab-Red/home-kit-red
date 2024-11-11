package red.tetracube.homekitred.iot.device.provisioning.models

sealed class FieldInputEvent {

    data class FieldValueInput(val fieldName: FieldName, val fieldValue: String) : FieldInputEvent()
    data class DeviceTypeSelect(val deviceTypeOption: DeviceTypeOptionModel) : FieldInputEvent()

    enum class FieldName {
        DEVICE_NAME,
        DEVICE_TYPE
    }

}
