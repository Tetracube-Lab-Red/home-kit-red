package red.tetracube.homekitred.iot.device.provisioning

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
import red.tetracube.homekitred.app.exceptions.HomeKitRedError
import red.tetracube.homekitred.data.enumerations.DeviceType
import red.tetracube.homekitred.iot.device.provisioning.models.DeviceProvisioningFormModel
import red.tetracube.homekitred.iot.device.provisioning.models.FieldInputEvent
import red.tetracube.homekitred.iot.device.provisioning.models.UPSProvisioningFormModel
import red.tetracube.homekitred.ui.core.models.UIState

class DeviceProvisioningViewModel(
    private val deviceProvisioningUseCase: DeviceProvisioningUseCase
) : ViewModel() {

    private val _deviceProvisioningFormState = mutableStateOf(DeviceProvisioningFormModel())
    val deviceProvisioningFormSate: State<DeviceProvisioningFormModel>
        get() = _deviceProvisioningFormState

    private val _upsProvisioningFormState = mutableStateOf(UPSProvisioningFormModel())
    val upsProvisioningFormState: State<UPSProvisioningFormModel>
        get() = _upsProvisioningFormState

    private val _uiState = mutableStateOf<UIState>(UIState.Neutral)
    val uiState: State<UIState>
        get() = _uiState

    private val formValidationUseCase = FormValidationUseCase()

    fun onSaveClick() {
        viewModelScope.launch {
            _uiState.value = UIState.Loading
            val deviceProvisioningResult = deviceProvisioningUseCase.sendDeviceProvisioningRequest(
                _deviceProvisioningFormState.value,
                _upsProvisioningFormState.value
            )
            if (deviceProvisioningResult.isSuccess) {
                _uiState.value = UIState.FinishedWithSuccess
            } else {
                _uiState.value = deviceProvisioningResult.exceptionOrNull()
                    ?.let { it as HomeKitRedError }
                    ?.let { UIState.FinishedWithError(it) }
                    ?: UIState.FinishedWithError(HomeKitRedError.GenericError)
            }
        }
    }

    fun onInputEvent(fieldInputEvent: FieldInputEvent) {
        when (fieldInputEvent) {
            is FieldInputEvent.FieldValueInput -> {

                when (fieldInputEvent.fieldName) {
                    FieldInputEvent.FieldName.DEVICE_NAME ->
                        _deviceProvisioningFormState.value =
                            _deviceProvisioningFormState.value.copy(
                                deviceName = _deviceProvisioningFormState.value.deviceName.copy(
                                    value = fieldInputEvent.fieldValue,
                                    isDirty = true
                                ),
                            )

                    FieldInputEvent.FieldName.NUT_HOST ->
                        _upsProvisioningFormState.value =
                            _upsProvisioningFormState.value.copy(
                                nutServerHost = _upsProvisioningFormState.value.nutServerHost.copy(
                                    value = fieldInputEvent.fieldValue,
                                    isDirty = true
                                ),
                            )

                    FieldInputEvent.FieldName.NUT_PORT ->
                        _upsProvisioningFormState.value =
                            _upsProvisioningFormState.value.copy(
                                nutServerPort = _upsProvisioningFormState.value.nutServerPort.copy(
                                    value = fieldInputEvent.fieldValue,
                                    isDirty = true
                                ),
                            )

                    FieldInputEvent.FieldName.NUT_UPS_ALIAS ->
                        _upsProvisioningFormState.value =
                            _upsProvisioningFormState.value.copy(
                                nutUPSAlias = _upsProvisioningFormState.value.nutUPSAlias.copy(
                                    value = fieldInputEvent.fieldValue,
                                    isDirty = true
                                ),
                            )

                    else -> {}
                }
            }

            is FieldInputEvent.DeviceTypeSelect ->
                _deviceProvisioningFormState.value = _deviceProvisioningFormState.value.copy(
                    deviceType = _deviceProvisioningFormState.value.deviceType.copy(
                        value = fieldInputEvent.deviceTypeOption.fieldValue,
                        isDirty = true,
                        internalType = fieldInputEvent.deviceTypeOption.deviceType
                    )
                )
        }
        validateForm()
    }

    private fun validateForm() {
        if (_deviceProvisioningFormState.value.deviceName.isDirty) {
            val (isValid, message) = formValidationUseCase.validateDeviceName(
                _deviceProvisioningFormState.value.deviceName.value
            )
            _deviceProvisioningFormState.value = _deviceProvisioningFormState.value.copy(
                deviceName = _deviceProvisioningFormState.value.deviceName.copy(
                    isValid = isValid,
                    validationMessage = message
                )
            )
        }
        if (_deviceProvisioningFormState.value.deviceType.isDirty) {
            val (isValid, message) = formValidationUseCase.validateDeviceType(
                _deviceProvisioningFormState.value.deviceType.internalType
            )
            _deviceProvisioningFormState.value = _deviceProvisioningFormState.value.copy(
                deviceType = _deviceProvisioningFormState.value.deviceType.copy(
                    isValid = isValid,
                    validationMessage = message
                )
            )
        }

        val subformIsValid =
            if (_deviceProvisioningFormState.value.deviceType.internalType == DeviceType.UPS) {
                validateUPSForm()
            } else {
                true
            }

        _deviceProvisioningFormState.value = _deviceProvisioningFormState.value.copy(
            formIsValid = (_deviceProvisioningFormState.value.deviceName.isValid && _deviceProvisioningFormState.value.deviceName.isDirty)
                    && (_deviceProvisioningFormState.value.deviceType.isValid && _deviceProvisioningFormState.value.deviceName.isDirty)
                    && subformIsValid
        )
    }

    fun validateUPSForm(): Boolean {
        if (_upsProvisioningFormState.value.nutServerHost.isDirty) {
            val (isValid, message) = formValidationUseCase.validateNutHost(
                _upsProvisioningFormState.value.nutServerHost.value
            )
            _upsProvisioningFormState.value = _upsProvisioningFormState.value.copy(
                nutServerHost = _upsProvisioningFormState.value.nutServerHost.copy(
                    isValid = isValid,
                    validationMessage = message
                )
            )
        }

        if (_upsProvisioningFormState.value.nutServerPort.isDirty) {
            val (isValid, message) = formValidationUseCase.validateNutPort(
                _upsProvisioningFormState.value.nutServerPort.value
            )
            _upsProvisioningFormState.value = _upsProvisioningFormState.value.copy(
                nutServerPort = _upsProvisioningFormState.value.nutServerPort.copy(
                    isValid = isValid,
                    validationMessage = message
                )
            )
        }

        if (_upsProvisioningFormState.value.nutUPSAlias.isDirty) {
            val (isValid, message) = formValidationUseCase.validateNutUPSAlias(
                _upsProvisioningFormState.value.nutUPSAlias.value
            )
            _upsProvisioningFormState.value = _upsProvisioningFormState.value.copy(
                nutUPSAlias = _upsProvisioningFormState.value.nutUPSAlias.copy(
                    isValid = isValid,
                    validationMessage = message
                )
            )
        }

        return (_upsProvisioningFormState.value.nutServerHost.isDirty && _upsProvisioningFormState.value.nutServerHost.isValid)
                && (_upsProvisioningFormState.value.nutServerPort.isDirty && _upsProvisioningFormState.value.nutServerPort.isValid)
                && (_upsProvisioningFormState.value.nutUPSAlias.isDirty && _upsProvisioningFormState.value.nutUPSAlias.isValid)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val homeKitRedContainer =
                    (this[APPLICATION_KEY] as HomeKitRedApp).homeKitRedContainer
                DeviceProvisioningViewModel(
                    deviceProvisioningUseCase = DeviceProvisioningUseCase(
                        hubDatasource = homeKitRedContainer.homeKitRedDatabase.hubRepository(),
                        deviceAPIRepository = homeKitRedContainer.deviceAPIRepository
                    )
                )
            }
        }
    }

}