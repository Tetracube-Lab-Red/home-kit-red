package red.tetracube.homekitred.iot.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import red.tetracube.homekitred.HomeKitRedApp
import red.tetracube.homekitred.app.models.UIState
import red.tetracube.homekitred.data.services.DeviceService
import red.tetracube.homekitred.iot.home.domain.models.BasicTelemetry
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

    private val _devicesTelemetriesMap = mutableStateMapOf<String, Pair<Device, BasicTelemetry>>()
    val devicesTelemetriesMap: Map<String, Pair<Device, BasicTelemetry>>
        get() = _devicesTelemetriesMap

    private var job = Job()
        get() {
            if (field.isCancelled) field = Job()
            return field
        }

    fun loadHubData() {
        viewModelScope.launch(job) {
            launch {
                _uiState.value = UIState.Loading
                ioTHomeUseCases.getHubWithRooms()
                    .collect {
                        _uiState.value = UIState.FinishedWithSuccessContent(it)
                    }
            }

            launch {
                ioTHomeUseCases.getDevices(null)
                    .collect { d ->
                        _devicesTelemetriesMap.putIfAbsent(d.slug, d to d.basicTelemetry)
                    }
            }

            launch {
                ioTHomeUseCases.listenDatabaseTelemetryStreaming()
                    .collect { telemetries ->
                        telemetries.forEach { telemetry ->
                            _devicesTelemetriesMap[telemetry.slug]
                                ?.first
                                ?.also { device ->
                                    _devicesTelemetriesMap[telemetry.slug] = device to telemetry
                                }
                        }
                    }
            }

            launch {
                ioTHomeUseCases.listenAPITelemetrySteaming()
            }
        }
    }

    fun cancelListeners() {
        job.cancel()
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