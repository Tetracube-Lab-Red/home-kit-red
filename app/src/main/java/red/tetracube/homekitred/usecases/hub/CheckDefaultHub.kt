package red.tetracube.homekitred.usecases.hub

import red.tetracube.homekitred.data.db.datasource.HubDatasource

class CheckDefaultHub(
    private val hubDatasource: HubDatasource
) {

    suspend operator fun invoke(): Boolean {
        return hubDatasource.getActiveHub() != null
    }

}