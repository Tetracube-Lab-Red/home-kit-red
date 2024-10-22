package red.tetracube.homekitred.iot.home.domain.mappers

import red.tetracube.homekitred.data.db.entities.HubWithRoomsEntity
import red.tetracube.homekitred.domain.Room
import red.tetracube.homekitred.iot.home.domain.models.HubWithRooms

fun HubWithRoomsEntity.toDomain() =
    HubWithRooms(
        slug = this.hub.slug,
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
                slug = "all",
                name = "House"
            )
        ) + this.rooms.map {
            Room(
                slug = it.slug,
                name = it.name
            )
        }
    )