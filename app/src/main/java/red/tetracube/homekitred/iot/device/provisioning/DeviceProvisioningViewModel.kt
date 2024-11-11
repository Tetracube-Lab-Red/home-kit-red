package red.tetracube.homekitred.iot.device.provisioning

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import red.tetracube.homekitred.HomeKitRedApp
import red.tetracube.homekitred.iot.device.provisioning.models.DeviceProvisioningUIModel
import red.tetracube.homekitred.iot.device.provisioning.models.FieldInputEvent

class DeviceProvisioningViewModel : ViewModel() {

    private val _deviceProvisioningFormState = mutableStateOf(DeviceProvisioningUIModel())
    val deviceProvisioningFormSate: State<DeviceProvisioningUIModel>
        get() = _deviceProvisioningFormState

    fun onInputEvent(fieldInputEvent: FieldInputEvent) {
        when (fieldInputEvent) {
            is FieldInputEvent.FieldValueInput -> {
                _deviceProvisioningFormState.value =
                    when (fieldInputEvent.fieldName) {
                        FieldInputEvent.FieldName.DEVICE_NAME -> _deviceProvisioningFormState.value.copy(
                            deviceName = _deviceProvisioningFormState.value.deviceName.copy(
                                value = fieldInputEvent.fieldValue,
                                isDirty = true
                            ),
                        )

                        else ->  _deviceProvisioningFormState.value
                    }
                //validateForm()
            }

            is FieldInputEvent.DeviceTypeSelect -> TODO()
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val homeKitRedContainer =
                    (this[APPLICATION_KEY] as HomeKitRedApp).homeKitRedContainer
                DeviceProvisioningViewModel()
            }
        }
    }

}