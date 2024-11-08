package red.tetracube.homekitred.splash

import red.tetracube.homekitred.data.db.datasource.HubDatasource
import red.tetracube.homekitred.data.services.HubLocalDataService
import red.tetracube.homekitred.splash.domain.mappers.toConnectInfo
import red.tetracube.homekitred.splash.domain.models.HubConnectionInfo

class SplashUseCases(
    private val hubDatasource: HubDatasource,
    private val hubLocalDataService: HubLocalDataService
) {

    suspend fun getHubConnectInfo(): HubConnectionInfo? {
        return hubDatasource.getActiveHub()?.toConnectInfo()
    }

    suspend fun retrieveLatestHubInfo(hubConnectionInfo: HubConnectionInfo) {
        hubLocalDataService.updateLocalHubData(
            hubConnectionInfo.slug,
            hubConnectionInfo.apiURI,
            hubConnectionInfo.token
        )
    }

}