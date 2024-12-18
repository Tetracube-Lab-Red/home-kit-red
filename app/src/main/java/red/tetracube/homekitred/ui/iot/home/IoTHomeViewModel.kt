package red.tetracube.homekitred.ui.iot.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import red.tetracube.homekitred.HomeKitRedApp
import red.tetracube.homekitred.business.usecases.DeviceUseCases
import red.tetracube.homekitred.data.db.HomeKitRedDatabase
import red.tetracube.homekitred.ui.iot.home.mappers.asBasicTelemetry
import red.tetracube.homekitred.ui.iot.home.mappers.toUIModel
import red.tetracube.homekitred.ui.iot.home.models.BasicTelemetry
import red.tetracube.homekitred.ui.state.UIState
import java.util.UUID

class IoTHomeViewModel(
    private val localDataSource: HomeKitRedDatabase,
    private val deviceUseCases: DeviceUseCases
) : ViewModel() {

    private val _uiState = mutableStateOf<UIState>(UIState.Neutral)
    val uiState: State<UIState>
        get() = _uiState

    private var job = Job()
        get() {
            if (field.isCancelled) field = Job()
            return field
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun iotData() =
        localDataSource.hubDataSource().getHubAndRooms()
            .map { it.toUIModel() }
            .flatMapMerge { iotData ->
                devicesFlow(iotData.hubId)
                    .flatMapMerge { devices ->
                        upsBasicTelemetryFlow()
                            .map { telemetries ->
                                val mappedDevices = devices.map { device ->
                                    val roomName = device.roomId?.let { roomId ->
                                        iotData.rooms.firstOrNull { room -> room.id == roomId }
                                    }?.name
                                    val telemetry =
                                        telemetries.firstOrNull { telemetry -> telemetry.deviceId == device.id }
                                    device.toUIModel(roomName, telemetry)
                                }
                                iotData.copy(
                                    devices = mappedDevices
                                )
                            }
                    }
            }
            .map { UIState.FinishedWithSuccessContent(it) }

    private fun devicesFlow(hubId: UUID) =
        localDataSource.deviceDataSource().getDevices(hubId)

    private fun upsBasicTelemetryFlow() =
        localDataSource.upsTelemetryDataSource().telemetriesLiveData()
            .map { telemetries ->
                telemetries.map { telemetry -> telemetry.asBasicTelemetry() as BasicTelemetry }
            }

    fun loadHubData() {
        viewModelScope.launch(job) {
            launch {
                deviceUseCases.listenDevicesStreams()
            }
            launch {
                deviceUseCases.listenDeviceTelemetryStreams()
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
                    localDataSource = homeKitRedContainer.homeKitRedDatabase,
                    deviceUseCases = DeviceUseCases(
                        localDataSource = homeKitRedContainer.homeKitRedDatabase,
                        ioTAPIDataSource = homeKitRedContainer.ioTAPIDataSource
                    )
                )
            }
        }
    }

}