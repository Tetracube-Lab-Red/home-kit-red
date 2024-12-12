package red.tetracube.homekitred.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import red.tetracube.homekitred.HomeKitRedApp
import red.tetracube.homekitred.data.services.HubLocalDataService
import red.tetracube.homekitred.business.models.ui.UIState
import red.tetracube.homekitred.data.db.datasource.HubDatasource
import red.tetracube.homekitred.data.mappers.toConnectInfo
import red.tetracube.homekitred.business.usecases.GlobalDataUseCases

class SplashViewModel(
    private val hubDatasource: HubDatasource,
    private val globalDataUseCases: GlobalDataUseCases
) : ViewModel() {

    /* private val _uiState = mutableStateOf<UIState>(UIState.Neutral)
     val uiState: State<UIState>
         get() = _uiState*/

    val uiState: MutableStateFlow<UIState> = MutableStateFlow<UIState>(
        UIState.Neutral
    )
    /*
        private val _hubActiveExists = mutableStateOf<Boolean?>(null)
        val hubActiveExists: State<Boolean?>
            get() = _hubActiveExists*/

    fun loadDefaultHub() {
        viewModelScope.launch {
            uiState.value = UIState.Loading
            hubDatasource.streamActiveHub()
                .map { nullableHub -> nullableHub?.toConnectInfo() }
                .map { nullableConnectInfo ->
                    if (nullableConnectInfo != null) {
                         val result = globalDataUseCases.updateLocalData(defaultHubConnectInfo)
                        //     if (result.isSuccess) {
                        UIState.FinishedWithSuccessContent(true)
                        /* } else {
                             result.exceptionOrNull()
                                 ?.let { it as HomeKitRedError }
                                 ?.let { UIState.FinishedWithError(it) }
                                 ?: UIState.FinishedWithError(HomeKitRedError.GenericError)
                         }*/
                    } else {
                        UIState.FinishedWithSuccessContent(false)
                    }
                }
                .collect { liveUIState ->
                    uiState.value = liveUIState
                }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val homeKitRedContainer =
                    (this[APPLICATION_KEY] as HomeKitRedApp).homeKitRedContainer
                SplashViewModel(
                    hubDatasource = homeKitRedContainer.homeKitRedDatabase.hubRepository(),
                    globalDataUseCases = GlobalDataUseCases(
                        hubDatasource = homeKitRedContainer.homeKitRedDatabase.hubRepository(),
                        roomAPIRepository = homeKitRedContainer.roomAPIRepository,
                        roomDatasource = homeKitRedContainer.homeKitRedDatabase.roomRepository(),
                    )
                )
            }
        }
    }
}