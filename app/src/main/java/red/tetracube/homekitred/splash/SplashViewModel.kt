package red.tetracube.homekitred.splash

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import red.tetracube.homekitred.HomeKitRedApp
import red.tetracube.homekitred.ui.core.models.UIState

class SplashViewModel(
    private val splashUseCases: SplashUseCases
) : ViewModel() {

    private val _uiState = mutableStateOf<UIState>(UIState.Neutral)
    val uiState: State<UIState>
        get() = _uiState

    private val _hubActiveExists = mutableStateOf<Boolean?>(null)
    val hubActiveExists: State<Boolean?>
        get() = _hubActiveExists

    suspend fun loadDefaultHub() {
        _uiState.value = UIState.Loading
        val defaultHubConnectInfo = splashUseCases.getHubConnectInfo()
        if (defaultHubConnectInfo != null) {
            _hubActiveExists.value = true
            splashUseCases.retrieveLatestHubInfo(defaultHubConnectInfo)
        } else {
            _hubActiveExists.value = false
        }
        _uiState.value = UIState.FinishedWithSuccess
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val homeKitRedContainer =
                    (this[APPLICATION_KEY] as HomeKitRedApp).homeKitRedContainer
                SplashViewModel(
                    splashUseCases = SplashUseCases(
                        hubDatasource = homeKitRedContainer.homeKitRedDatabase.hubRepository(),
                        hubAPI = homeKitRedContainer.hubAPIRepository,
                        roomDatasource = homeKitRedContainer.homeKitRedDatabase.roomRepository(),
                        roomAPIRepository = homeKitRedContainer.roomAPIRepository
                    )
                )
            }
        }
    }
}