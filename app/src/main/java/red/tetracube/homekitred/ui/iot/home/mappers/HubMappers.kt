package red.tetracube.homekitred.ui.iot.home.mappers

import red.tetracube.homekitred.data.db.entities.HubWithRoomsEntity
import red.tetracube.homekitred.ui.iot.home.models.IoTDashboardModel
import red.tetracube.homekitred.ui.iot.home.models.Room

fun HubWithRoomsEntity.toUIModel() =
    IoTDashboardModel(
        hubId = this.hub.id,
        avatarName = if (this.hub.name.contains(" ")) {
            this.hub.name.split(" ")
                .map { it.first() }
                .joinToString("")
        } else {
            this.hub.name.take(2)
        },
        hubName = this.hub.name,
        rooms = listOf<Room>(
            Room(
                id = null,
                name = "House"
            )
        ) + this.rooms.map {
            Room(
                id = it.id,
                name = it.name
            )
        }
    )