package red.tetracube.homekitred.ui.login.models

sealed class FieldInputEvent {

    data class FieldFocusAcquire(val fieldName: FieldName): FieldInputEvent()
    data class FieldValueInput(val fieldName: FieldName, val fieldValue: String): FieldInputEvent()
    data class FieldTrailingButtonClick(val fieldName: FieldName) : FieldInputEvent()

    enum class FieldName {
        HUB_ADDRESS,
        HUB_NAME,
        PASSWORD
    }

}
