package red.tetracube.homekitred.iot.home.domain.mappers

import red.tetracube.homekitred.data.db.entities.HubWithRoomsEntity
import red.tetracube.homekitred.iot.home.domain.models.Room
import red.tetracube.homekitred.iot.home.domain.models.HubWithRooms
import java.util.UUID

fun HubWithRoomsEntity.toDomain() =
    HubWithRooms(
        id = this.hub.id,
        avatarName = if (this.hub.name.contains(" ")) {
            this.hub.name.split(" ")
                .map { it.first() }
                .joinToString("")
        } else {
            this.hub.name.take(2)
        },
        name = this.hub.name,
        rooms = listOf<Room>(
            Room(
                id = UUID.randomUUID(),
                name = "House"
            )
        ) + this.rooms.map {
            Room(
                id = it.id,
                name = it.name
            )
        }
    )