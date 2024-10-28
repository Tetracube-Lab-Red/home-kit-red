package red.tetracube.homekitred.hubcentral.room.create.models

data class RoomCreateUIModel(
    val roomNameField: RoomNameField = RoomNameField(),
    val formIsValid: Boolean = false
)

data class RoomNameField(
    val isTouched: Boolean = false,
    val isDirty: Boolean = false,
    val isValid: Boolean = false,
    val supportingText: String = "Required",
    val value: String = ""
)
