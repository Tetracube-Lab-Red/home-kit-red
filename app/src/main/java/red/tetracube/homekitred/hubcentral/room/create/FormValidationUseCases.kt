package red.tetracube.homekitred.hubcentral.room.create

class FormValidationUseCases {

    fun roomNameHasError(roomName: String) =
        if (roomName.isBlank()) {
            false to "Required"
        } else if (!(5..30).contains(roomName.length)) {
            false to "Name too short"
        } else if (!roomName.matches("^[ \\w]+\$".toRegex())) {
            false to "Name should contain only alphanumeric or spaces"
        } else {
            true to "✅ OK"
        }

}