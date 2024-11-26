package red.tetracube.homekitred.iot.device.room

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.launch
import red.tetracube.homekitred.HomeKitRedApp

class DeviceRoomViewModel(
    private val deviceSlug: String,
    private val deviceRoomUseCases: DeviceRoomUseCases
) : ViewModel() {

    private var _roomSlug = mutableStateOf("")
    val roomSlug: State<String>
        get() = _roomSlug

    private var _roomsMap = mutableMapOf<String, String>()
    val roomsMap: Map<String, String>
        get() = _roomsMap

    fun loadRooms() {
        viewModelScope.launch {
            _roomsMap.putAll(deviceRoomUseCases.getRoomsMap())
            _roomSlug.value = deviceRoomUseCases.getDeviceRoom(deviceSlug) ?: ""
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
                        homeKitRedContainer.homeKitRedDatabase.roomRepository()
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