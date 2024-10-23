package red.tetracube.homekitred.iot.home

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import red.tetracube.homekitred.data.db.datasource.HubDatasource
import red.tetracube.homekitred.iot.home.domain.models.HubWithRooms
import red.tetracube.homekitred.iot.home.domain.mappers.toDomain

class IoTHomeUseCases(
    private val hubDatasource: HubDatasource
) {

    fun getHubWithRooms(): Flow<HubWithRooms> =
        hubDatasource.getHubAndRooms()
            .map { it.toDomain() }

}