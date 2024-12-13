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
import red.tetracube.homekitred.business.models.errors.HomeKitRedError
import red.tetracube.homekitred.business.models.ui.UIState
import red.tetracube.homekitred.data.db.datasource.HubDataSource
import red.tetracube.homekitred.business.mappers.toConnectInfo
import red.tetracube.homekitred.business.usecases.GlobalDataUseCases

class SplashViewModel(
    private val hubDatasource: HubDataSource,
    private val globalDataUseCases: GlobalDataUseCases
) : ViewModel() {

    val uiState: MutableStateFlow<UIState> = MutableStateFlow<UIState>(
        UIState.Neutral
    )

    fun loadDefaultHub() {
        viewModelScope.launch {
            uiState.value = UIState.Loading
            hubDatasource.streamActiveHub()
                .map { nullableHub -> nullableHub?.toConnectInfo() }
                .map { nullableConnectInfo ->
                    if (nullableConnectInfo != null) {
                        val result = globalDataUseCases.updateLocalData(nullableConnectInfo)
                        if (result.isSuccess) {
                            UIState.FinishedWithSuccessContent(true)
                        } else {
                            result.exceptionOrNull()
                                ?.let { it as HomeKitRedError }
                                ?.let { UIState.FinishedWithError(it) }
                                ?: UIState.FinishedWithError(HomeKitRedError.GenericError)
                        }
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
                        roomDatasource = homeKitRedContainer.homeKitRedDatabase.roomRepository(),
                        hubAPIDataSource = homeKitRedContainer.hubAPIDataSource
                    )
                )
            }
        }
    }
}