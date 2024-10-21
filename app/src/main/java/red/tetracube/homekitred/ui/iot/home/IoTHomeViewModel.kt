package red.tetracube.homekitred.ui.iot.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import red.tetracube.homekitred.HomeKitRedApp
import red.tetracube.homekitred.domain.HubWithRooms
import red.tetracube.homekitred.usecases.hub.GetHubWithRooms

class IoTHomeViewModel(
    private val getHubWithRooms: GetHubWithRooms
) : ViewModel() {

    private val _hub = mutableStateOf<HubWithRooms?>(null)
    val hub: State<HubWithRooms?>
        get() = _hub

    fun loadHubData() {
        _hub.value = getHubWithRooms.invoke()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val homeKitRedContainer =
                    (this[APPLICATION_KEY] as HomeKitRedApp).homeKitRedContainer
                IoTHomeViewModel(
                    getHubWithRooms = GetHubWithRooms(
                        hubDatasource = homeKitRedContainer.homeKitRedDatabase.hubRepository()
                    )
                )
            }
        }
    }

}