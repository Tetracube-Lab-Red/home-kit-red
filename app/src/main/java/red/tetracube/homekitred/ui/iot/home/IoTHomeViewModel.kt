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
import red.tetracube.homekitred.data.db.datasource.DeviceDataSource
import red.tetracube.homekitred.data.db.datasource.HubDataSource
import red.tetracube.homekitred.ui.iot.home.mappers.toUIModel
import red.tetracube.homekitred.ui.state.UIState

class IoTHomeViewModel(
    private val hubDataSource: HubDataSource,
    private val deviceDataSource: DeviceDataSource,
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
        hubDataSource.getHubAndRooms()
            .map { it.toUIModel() }
            .flatMapMerge { iotData ->
                deviceDataSource.getDevices(iotData.hubId)
                    .map { devices ->
                        devices.map { device ->
                            val roomName = device.roomId?.let { roomId ->
                                iotData.rooms.firstOrNull { room -> room.id == roomId }
                            }?.name
                            device.toUIModel(roomName)
                        }
                    }
                    .map { devices ->
                        iotData.copy(
                            devices = devices
                        )
                    }
            }
            .map { UIState.FinishedWithSuccessContent(it) }

    /* suspend fun getDevices(roomSlug: String?): Flow<Device> {
         val hub = hubDatasource.getActiveHub()!!
         return database.deviceRepository().getDevices(hub.slug)
             .filter { entity ->
                 if (roomSlug == null) true else entity.roomSlug == roomSlug
             }
             .map { entity ->
                 Device(
                     name = entity.name,
                     slug = entity.slug,
                     roomName = entity.roomSlug?.let {
                         database.roomRepository().getBySlug(it).name
                     },
                     roomSlug = entity.roomSlug,
                     notifications = 0,
                     type = entity.type
                 )
             }
     }*/

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
                    /* ioTHomeUseCases = IoTHomeUseCases(
                         hubDatasource = homeKitRedContainer.homeKitRedDatabase.hubRepository(),
                         database = homeKitRedContainer.homeKitRedDatabase,
                         deviceService = DeviceService(
                             ioTAPIDataSource = homeKitRedContainer.ioTAPIDataSource,
                             database = homeKitRedContainer.homeKitRedDatabase
                         )
                     )*/
                    hubDataSource = homeKitRedContainer.homeKitRedDatabase.hubDataSource(),
                    deviceDataSource = homeKitRedContainer.homeKitRedDatabase.deviceDataSource(),
                    deviceUseCases = DeviceUseCases(
                        hubDataSource = homeKitRedContainer.homeKitRedDatabase.hubDataSource(),
                        ioTAPIDataSource = homeKitRedContainer.ioTAPIDataSource,
                        deviceDataSource = homeKitRedContainer.homeKitRedDatabase.deviceDataSource()
                    )
                )
            }
        }
    }

}