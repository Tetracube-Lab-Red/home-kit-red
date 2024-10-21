package red.tetracube.homekitred.ui.splash

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import red.tetracube.homekitred.HomeKitRedApp
import red.tetracube.homekitred.ui.core.models.UIState
import red.tetracube.homekitred.usecases.hub.CheckDefaultHub
import red.tetracube.homekitred.usecases.hub.GetLatestHubData

class SplashViewModel(
    private val checkDefaultHub: CheckDefaultHub,
    private val getLatestHubData: GetLatestHubData
) : ViewModel() {

    private val _uiState = mutableStateOf<UIState>(UIState.Neutral)
    val uiState: State<UIState>
        get() = _uiState

    private val _hubActiveExists = mutableStateOf<Boolean?>(null)
    val hubActiveExists: State<Boolean?>
        get() = _hubActiveExists

    suspend fun checkDefaultHub() {
        _uiState.value = UIState.Loading
        val defaultHubConnectInfo = checkDefaultHub.invoke()
        _hubActiveExists.value = defaultHubConnectInfo != null
        if (_hubActiveExists.value == true) {
            getLatestHubData.invoke(defaultHubConnectInfo!!)
        }
        _uiState.value = UIState.FinishedWithSuccess
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val homeKitRedContainer =
                    (this[APPLICATION_KEY] as HomeKitRedApp).homeKitRedContainer
                val createHubUseCase = CheckDefaultHub(
                    hubDatasource = homeKitRedContainer.homeKitRedDatabase.hubRepository()
                )
                val getLatestHubData = GetLatestHubData(
                    hubAPI = homeKitRedContainer.hubAPIRepository,
                    roomDatasource = homeKitRedContainer.homeKitRedDatabase.roomRepository()
                )
                SplashViewModel(
                    checkDefaultHub = createHubUseCase,
                    getLatestHubData = getLatestHubData
                )
            }
        }
    }
}