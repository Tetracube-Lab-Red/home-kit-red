package red.tetracube.homekitred.iot.device.room

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
import red.tetracube.homekitred.business.models.errors.HomeKitRedError
import red.tetracube.homekitred.business.models.ui.UIState

class DeviceRoomViewModel(
    private val deviceSlug: String,
    private val deviceRoomUseCases: DeviceRoomUseCases
) : ViewModel() {

    private var _roomSlug = mutableStateOf<String>("")
    val roomSlug: State<String>
        get() = _roomSlug

    private var _uiState = mutableStateOf<UIState>(UIState.Neutral)
    val uiState: State<UIState>
        get() = _uiState

    private var _roomsMap = mutableMapOf<String, String>()
    val roomsMap: Map<String, String>
        get() = _roomsMap

    private var _deviceName = mutableStateOf<String>("")
    val deviceName: State<String>
        get() = _deviceName

    fun loadRooms() {
        viewModelScope.launch {
            _uiState.value = UIState.Loading
            val device = deviceRoomUseCases.getDeviceRoom(deviceSlug)
            _roomsMap.putAll(deviceRoomUseCases.getRoomsMap())
            if (device != null) {
                _roomSlug.value = device.second ?: ""
                _deviceName.value = device.first
                _uiState.value = UIState.Neutral
            } else {
                _uiState.value = UIState.FinishedWithError(HomeKitRedError.NotFound)
            }
        }
    }

    fun setRoomSlug(newSlug: String) {
        _roomSlug.value = newSlug
    }

    fun submitDeviceRoomJoin() {
        viewModelScope.launch {
            _uiState.value = UIState.Loading
            val updateResult = deviceRoomUseCases.updateDeviceRoom(deviceSlug, _roomSlug.value)
            if (updateResult.isSuccess) {
                _uiState.value = UIState.FinishedWithSuccess
            } else {
                _uiState.value = updateResult.exceptionOrNull()
                    ?.let { it as? HomeKitRedError }
                    ?.let { UIState.FinishedWithError(it) }
                    ?:  UIState.FinishedWithError(HomeKitRedError.GenericError)
            }
        }
    }

    companion object {
        val Factory: (String) -> ViewModelProvider.Factory = { deviceSlug ->
            viewModelFactory {
                initializer {
                    val homeKitRedContainer =
                        (this[APPLICATION_KEY] as HomeKitRedApp).homeKitRedContainer
                    val deviceRoomUseCases = DeviceRoomUseCases(
                        homeKitRedContainer.homeKitRedDatabase.hubRepository(),
                        homeKitRedContainer.homeKitRedDatabase.roomRepository(),
                        homeKitRedContainer.homeKitRedDatabase.deviceRepository(),
                        homeKitRedContainer.deviceRoomAPIRepository
                    )
                    DeviceRoomViewModel(
                        deviceSlug = deviceSlug,
                        deviceRoomUseCases = deviceRoomUseCases
                    )
                }
            }
        }
    }

}