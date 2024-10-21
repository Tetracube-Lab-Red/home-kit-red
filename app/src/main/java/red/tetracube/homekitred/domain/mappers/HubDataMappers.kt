package red.tetracube.homekitred.domain.mappers

import red.tetracube.homekitred.data.db.entities.HubWithRoomsEntity
import red.tetracube.homekitred.domain.HubWithRooms
import red.tetracube.homekitred.domain.Room

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
        rooms = this.rooms.map {
            Room(
                slug = it.slug,
                name = it.name
            )
        }
    )