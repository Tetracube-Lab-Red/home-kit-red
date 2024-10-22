package red.tetracube.homekitred.hubcentral.room.create.models

data class RoomCreateUIModel(
    val roomNameField: RoomNameField = RoomNameField(),
    val formIsValid: Boolean = false
)

data class RoomNameField(
    val isTouched: Boolean = false,
    val hasError: Boolean = false,
    val value: String = ""
)
