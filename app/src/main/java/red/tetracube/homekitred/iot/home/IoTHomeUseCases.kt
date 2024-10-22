package red.tetracube.homekitred.iot.home

import red.tetracube.homekitred.data.db.datasource.HubDatasource
import red.tetracube.homekitred.iot.home.domain.models.HubWithRooms
import red.tetracube.homekitred.iot.home.domain.mappers.toDomain

class IoTHomeUseCases(
    private val hubDatasource: HubDatasource
) {

    suspend fun getHubWithRooms(): HubWithRooms =
        hubDatasource.getHubAndRooms().toDomain()

}