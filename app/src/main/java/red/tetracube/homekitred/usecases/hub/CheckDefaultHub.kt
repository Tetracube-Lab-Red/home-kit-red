package red.tetracube.homekitred.usecases.hub

import red.tetracube.homekitred.data.db.datasource.HubDatasource
import red.tetracube.homekitred.domain.HubConnectionInfo
import red.tetracube.homekitred.domain.mappers.toConnectInfo

class CheckDefaultHub(
    private val hubDatasource: HubDatasource
) {

    suspend operator fun invoke(): HubConnectionInfo? {
        return hubDatasource.getActiveHub()?.toConnectInfo()
    }

}