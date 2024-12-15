package red.tetracube.homekitred.ui.hub.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import red.tetracube.homekitred.HomeKitRedApp
import red.tetracube.homekitred.models.errors.HomeKitRedError
import red.tetracube.homekitred.ui.state.UIState
import red.tetracube.homekitred.business.usecases.GlobalDataUseCases
import red.tetracube.homekitred.business.usecases.HubUseCases

class HubSetupViewModel(
    private val hubUseCases: HubUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState>(UIState.Neutral)
    val uiState: StateFlow<UIState>
        get() = _uiState

    fun onSetupButtonClick(hubAddress: String, hubName: String, hubPassword: String) {
        _uiState.value = UIState.Loading
        viewModelScope.launch {
            val createHubResult = hubUseCases.create(
                hubAddress,
                hubName,
                hubPassword
            )
            if (createHubResult.isFailure) {
                _uiState.value = createHubResult.exceptionOrNull()
                    ?.let { it as HomeKitRedError }
                    ?.let { UIState.FinishedWithError(it) }
                    ?: UIState.FinishedWithError(HomeKitRedError.GenericError)
            } else {
                _uiState.value = UIState.FinishedWithSuccessContent(Unit)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val homeKitRedContainer =
                    (this[APPLICATION_KEY] as HomeKitRedApp).homeKitRedContainer
                HubSetupViewModel(
                    hubUseCases = HubUseCases(
                        hubAPIDataSource = homeKitRedContainer.hubAPIDataSource,
                        hubDataSource = homeKitRedContainer.homeKitRedDatabase.hubDataSource(),
                        globalDataUseCases = GlobalDataUseCases(
                            roomDatasource = homeKitRedContainer.homeKitRedDatabase.roomDataSource(),
                            hubAPIDataSource = homeKitRedContainer.hubAPIDataSource
                        )
                    )
                )
            }
        }
    }
}