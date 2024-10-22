package red.tetracube.homekitred.hubcentral.room.create.models

sealed class FieldInputEvent {

    data class FieldFocusAcquire(val fieldName: FieldName): FieldInputEvent()
    data class FieldValueInput(val fieldName: FieldName, val fieldValue: String): FieldInputEvent()

    enum class FieldName {
        ROOM_NAME
    }

}
