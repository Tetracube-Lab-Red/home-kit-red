package red.tetracube.homekitred.iot.device.room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import red.tetracube.homekitred.HomeKitRedApp

class DeviceRoomViewModel(private val deviceSlug: String) : ViewModel() {

    companion object {
        val Factory: (String) -> ViewModelProvider.Factory = { deviceSlug ->
            viewModelFactory {
                initializer {
                    val homeKitRedContainer =
                        (this[APPLICATION_KEY] as HomeKitRedApp).homeKitRedContainer
                    DeviceRoomViewModel(
                        deviceSlug = deviceSlug
                    )
                }
            }
        }
    }

}