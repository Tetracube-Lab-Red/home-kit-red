package red.tetracube.homekitred.iot.home

import androidx.compose.runtime.State
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
import red.tetracube.homekitred.ui.state.UIState
import red.tetracube.homekitred.business.services.DeviceService
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
            _uiState.value = UIState.Loading
            launch {
                ioTHomeUseCases.loadData()
                    .collect {
                        _hub.value = it
                        _uiState.value = UIState.FinishedWithSuccess
                    }
            }

            launch {
                ioTHomeUseCases.getDevices(null)
                    .collect { d ->
                        if (_devicesTelemetriesMap.containsKey(d.slug)) {
                            _devicesTelemetriesMap.replace(d.slug,  d to ioTHomeUseCases.getLatestTelemetry(d.slug))
                        } else {
                            _devicesTelemetriesMap.putIfAbsent(
                                d.slug,
                                d to ioTHomeUseCases.getLatestTelemetry(d.slug)
                            )
                        }
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
                            ioTAPIDataSource = homeKitRedContainer.ioTAPIDataSource,
                            database = homeKitRedContainer.homeKitRedDatabase
                        )
                    )
                )
            }
        }
    }

}