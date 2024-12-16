package red.tetracube.homekitred.ui.iot.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import red.tetracube.homekitred.HomeKitRedApp
import red.tetracube.homekitred.business.usecases.DeviceUseCases
import red.tetracube.homekitred.data.db.datasource.HubDataSource
import red.tetracube.homekitred.iot.home.domain.models.BasicTelemetry
import red.tetracube.homekitred.iot.home.domain.models.HubWithRooms
import red.tetracube.homekitred.ui.iot.home.mappers.toUIModel
import red.tetracube.homekitred.ui.iot.home.models.Device
import red.tetracube.homekitred.ui.state.UIState

class IoTHomeViewModel(
    private val hubDataSource: HubDataSource,
    private val deviceUseCases: DeviceUseCases
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

    @OptIn(ExperimentalCoroutinesApi::class)
    fun iotData() =
        hubDataSource.getHubAndRooms()
            .map { it.toUIModel() }
            .flatMapMerge { iotData ->
                flow {
                    emit(listOf("1", "2", "3", "4", "5", "6"))
                    delay(2000)
                    emit(listOf("7", "8", "9", "10", "11", "12"))
                }
                    .map { device ->
                        iotData.copy(
                            list = iotData.list + device
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
                /*ioTHomeUseCases.getDevices(null)
                    .collect { d ->
                        if (_devicesTelemetriesMap.containsKey(d.slug)) {
                            _devicesTelemetriesMap.replace(d.slug,  d to ioTHomeUseCases.getLatestTelemetry(d.slug))
                        } else {
                            _devicesTelemetriesMap.putIfAbsent(
                                d.slug,
                                d to ioTHomeUseCases.getLatestTelemetry(d.slug)
                            )
                        }
                    }*/
            }

            launch {
                /*   ioTHomeUseCases.listenDatabaseTelemetryStreaming()
                       .collect { telemetries ->
                           telemetries.forEach { telemetry ->
                               _devicesTelemetriesMap[telemetry.slug]
                                   ?.first
                                   ?.also { device ->
                                       _devicesTelemetriesMap[telemetry.slug] = device to telemetry
                                   }
                           }
                       }*/
            }

            launch {
                //  ioTHomeUseCases.listenAPITelemetrySteaming()
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