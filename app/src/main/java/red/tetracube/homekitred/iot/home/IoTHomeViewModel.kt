package red.tetracube.homekitred.iot.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import red.tetracube.homekitred.HomeKitRedApp
import red.tetracube.homekitred.app.models.UIState
import red.tetracube.homekitred.data.services.DeviceService
import red.tetracube.homekitred.iot.home.domain.models.Device
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

    private val _devices = mutableStateListOf<Device>()
    val devices: List<Device>
        get() = _devices

    fun loadHubData() {
        viewModelScope.launch {
            launch {
                _uiState.value = UIState.Loading
                ioTHomeUseCases.getHubWithRooms()
                    .collect {
                        _uiState.value = UIState.FinishedWithSuccessContent(it)
                    }
            }

            launch {
                ioTHomeUseCases.getDevices(null)
                    .filter { d -> !_devices.contains(d) }
                    .collect { d ->
                        _devices.add(d)
                    }
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
                        hubDatasource = homeKitRedContainer.homeKitRedDatabase.hubRepository(),
                        database = homeKitRedContainer.homeKitRedDatabase,
                        deviceService = DeviceService(
                            deviceAPIRepository = homeKitRedContainer.deviceAPIRepository,
                            database = homeKitRedContainer.homeKitRedDatabase
                        )
                    )
                )
            }
        }
    }

}