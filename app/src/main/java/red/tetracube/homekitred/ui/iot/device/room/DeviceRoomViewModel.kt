package red.tetracube.homekitred.ui.iot.device.room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import red.tetracube.homekitred.HomeKitRedApp
import red.tetracube.homekitred.business.usecases.DeviceRoomUseCases
import red.tetracube.homekitred.data.db.datasource.DeviceDataSource
import red.tetracube.homekitred.models.RoomSelectItem
import red.tetracube.homekitred.models.errors.HomeKitRedError
import red.tetracube.homekitred.ui.state.UIState
import java.util.UUID

class DeviceRoomViewModel(
    private val deviceId: UUID,
    private val deviceDataSource: DeviceDataSource,
    private val deviceRoomUseCases: DeviceRoomUseCases
) : ViewModel() {

    private var _uiState = MutableStateFlow<UIState>(UIState.Neutral)
    val uiState: StateFlow<UIState>
        get() = _uiState

    fun loadDevice(): Flow<String> =
        flow {
            deviceDataSource.getDeviceById(deviceId)
        }

    fun loadRooms(): Flow<List<RoomSelectItem>> =
        flow<List<RoomSelectItem>> {
            _uiState.value = UIState.Loading
            emit(deviceRoomUseCases.getRoomsForDeviceSelect(deviceId))
            _uiState.value = UIState.Neutral
        }

    fun submitDeviceRoomJoin(roomId: UUID) {
        viewModelScope.launch {
            _uiState.value = UIState.Loading
            val updateResult = deviceRoomUseCases.updateDeviceRoom(deviceId, roomId)
            if (updateResult.isSuccess) {
                _uiState.value = UIState.FinishedWithSuccessContent(Unit)
            } else {
                _uiState.value = updateResult.exceptionOrNull()
                    ?.let { it as? HomeKitRedError }
                    ?.let { UIState.FinishedWithError(it) }
                    ?: UIState.FinishedWithError(HomeKitRedError.GenericError)
            }
        }
    }

    companion object {
        val Factory: (UUID) -> ViewModelProvider.Factory = { deviceId ->
            viewModelFactory {
                initializer {
                    val homeKitRedContainer =
                        (this[APPLICATION_KEY] as HomeKitRedApp).homeKitRedContainer
                    DeviceRoomViewModel(
                        deviceId = deviceId,
                        deviceDataSource = homeKitRedContainer.homeKitRedDatabase.deviceDataSource(),
                        deviceRoomUseCases = DeviceRoomUseCases(
                            hubDataSource = homeKitRedContainer.homeKitRedDatabase.hubDataSource(),
                            deviceDataSource = homeKitRedContainer.homeKitRedDatabase.deviceDataSource(),
                            roomDataSource = homeKitRedContainer.homeKitRedDatabase.roomDataSource(),
                            ioTAPIDataSource = homeKitRedContainer.ioTAPIDataSource
                        )
                    )
                }
            }
        }
    }

}