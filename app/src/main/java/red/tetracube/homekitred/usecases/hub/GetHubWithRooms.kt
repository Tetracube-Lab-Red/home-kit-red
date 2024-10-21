package red.tetracube.homekitred.usecases.hub

import red.tetracube.homekitred.data.db.datasource.HubDatasource
import red.tetracube.homekitred.domain.HubWithRooms
import red.tetracube.homekitred.domain.mappers.toDomain

class GetHubWithRooms(
    private val hubDatasource: HubDatasource
) {

    suspend operator fun invoke(): HubWithRooms =
        hubDatasource.getHubAndRooms().toDomain()

}