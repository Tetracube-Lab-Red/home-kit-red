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
import red.tetracube.homekitred.business.models.errors.HomeKitRedError
import red.tetracube.homekitred.business.models.ui.UIState
import red.tetracube.homekitred.business.usecases.DeviceUseCase

class DeviceProvisioningViewModel(
    private val deviceUseCase: DeviceUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState>(UIState.Neutral)
    val uiState: StateFlow<UIState>
        get() = _uiState

    fun onSaveClick() {
        viewModelScope.launch {
            _uiState.value = UIState.Loading
            val deviceProvisioningResult = deviceUseCase.sendDeviceProvisioningRequest(
                _deviceProvisioningFormState.value,
                _upsProvisioningFormState.value
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
                    deviceUseCase = DeviceUseCase(
                        hubDatasource = homeKitRedContainer.homeKitRedDatabase.hubDataSource(),
                        ioTAPIDataSource = homeKitRedContainer.ioTAPIDataSource,
                        deviceDataSource = homeKitRedContainer.homeKitRedDatabase.deviceDataSource()
                    )
                )
            }
        }
    }

}