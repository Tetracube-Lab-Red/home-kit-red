package red.tetracube.homekitred.ui.hub.room.create

fun validateRoomName(roomName: String) =
    if (roomName.isBlank()) {
        false to "Required"
    } else if (!(5..30).contains(roomName.length)) {
        false to "Name too short"
    } else if (!roomName.matches("^[ \\w]+\$".toRegex())) {
        false to "Name should contain only alphanumeric or spaces"
    } else {
        true to "✅ OK"
    }