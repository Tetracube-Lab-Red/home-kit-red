package red.tetracube.homekitred.ui.iot.device.provisioning

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import red.tetracube.homekitred.HomeKitRedApp
import red.tetracube.homekitred.models.errors.HomeKitRedError
import red.tetracube.homekitred.ui.state.UIState
import red.tetracube.homekitred.business.usecases.DeviceUseCases
import red.tetracube.homekitred.models.DeviceProvisioning
import red.tetracube.homekitred.models.UPSProvisioning

class DeviceProvisioningViewModel(
    private val deviceUseCases: DeviceUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState>(UIState.Neutral)
    val uiState: StateFlow<UIState>
        get() = _uiState

    fun onSaveClick(deviceProvisioning: DeviceProvisioning, upsProvisioning: UPSProvisioning) {
        viewModelScope.launch {
            _uiState.value = UIState.Loading
            val deviceProvisioningResult = deviceUseCases.sendDeviceProvisioningRequest(
                deviceProvisioning,
                upsProvisioning
            )
            if (deviceProvisioningResult.isSuccess) {
                _uiState.value = UIState.FinishedWithSuccessContent(Unit)
            } else {
                _uiState.value = deviceProvisioningResult.exceptionOrNull()
                    ?.let { it as HomeKitRedError }
                    ?.let { UIState.FinishedWithError(it) }
                    ?: UIState.FinishedWithError(HomeKitRedError.GenericError)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val homeKitRedContainer =
                    (this[APPLICATION_KEY] as HomeKitRedApp).homeKitRedContainer
                DeviceProvisioningViewModel(
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