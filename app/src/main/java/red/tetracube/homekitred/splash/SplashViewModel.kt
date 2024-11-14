package red.tetracube.homekitred.splash

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.launch
import red.tetracube.homekitred.HomeKitRedApp
import red.tetracube.homekitred.app.exceptions.HomeKitRedError
import red.tetracube.homekitred.data.services.HubLocalDataService
import red.tetracube.homekitred.app.models.UIState

class SplashViewModel(
    private val splashUseCases: SplashUseCases
) : ViewModel() {

    private val _uiState = mutableStateOf<UIState>(UIState.Neutral)
    val uiState: State<UIState>
        get() = _uiState

    private val _hubActiveExists = mutableStateOf<Boolean?>(null)
    val hubActiveExists: State<Boolean?>
        get() = _hubActiveExists

    fun loadDefaultHub() {
        viewModelScope.launch {
            _uiState.value = UIState.Loading
            val defaultHubConnectInfo = splashUseCases.getHubConnectInfo()
            if (defaultHubConnectInfo != null) {
                _hubActiveExists.value = true
                val result = splashUseCases.retrieveLatestHubInfo(defaultHubConnectInfo)
                if (result.isSuccess) {
                    _uiState.value = UIState.FinishedWithSuccess
                } else {
                    _uiState.value = result.exceptionOrNull()
                        ?.let { it as HomeKitRedError }
                        ?.let { UIState.FinishedWithError(it) }
                        ?: UIState.FinishedWithError(HomeKitRedError.GenericError)
                }
            } else {
                _hubActiveExists.value = false
                _uiState.value = UIState.FinishedWithSuccess
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val homeKitRedContainer =
                    (this[APPLICATION_KEY] as HomeKitRedApp).homeKitRedContainer
                SplashViewModel(
                    splashUseCases = SplashUseCases(
                        hubDatasource = homeKitRedContainer.homeKitRedDatabase.hubRepository(),
                        hubLocalDataService = HubLocalDataService(
                            roomAPIRepository = homeKitRedContainer.roomAPIRepository,
                            roomDatasource = homeKitRedContainer.homeKitRedDatabase.roomRepository(),
                        )
                    )
                )
            }
        }
    }
}