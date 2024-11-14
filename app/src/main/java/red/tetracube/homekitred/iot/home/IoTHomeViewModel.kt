package red.tetracube.homekitred.iot.home

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
import red.tetracube.homekitred.app.models.UIState
import red.tetracube.homekitred.iot.home.domain.models.HubWithRooms

class IoTHomeViewModel(
    private val ioTHomeUseCases: IoTHomeUseCases
) : ViewModel() {

    private val _uiState = mutableStateOf<UIState>(UIState.Neutral)
    val uiState: State<UIState>
        get() = _uiState

    private val _hub = mutableStateOf<HubWithRooms?>(null)
    val hub: State<HubWithRooms?>
        get() = _hub

    fun loadHubData() {
        viewModelScope.launch {
            _uiState.value = UIState.Loading
            ioTHomeUseCases.getHubWithRooms()
                .collect {
                    _uiState.value = UIState.FinishedWithSuccessContent(it)
                }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val homeKitRedContainer =
                    (this[APPLICATION_KEY] as HomeKitRedApp).homeKitRedContainer
                IoTHomeViewModel(
                    ioTHomeUseCases = IoTHomeUseCases(
                        hubDatasource = homeKitRedContainer.homeKitRedDatabase.hubRepository()
                    )
                )
            }
        }
    }

}