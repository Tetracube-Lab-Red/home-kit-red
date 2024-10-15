package red.tetracube.homekitred.ui.setup

import android.webkit.URLUtil
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import red.tetracube.homekitred.HomeKitRedApp
import red.tetracube.homekitred.ui.login.models.FieldInputEvent
import red.tetracube.homekitred.ui.setup.models.HubSetupUIModel
import red.tetracube.homekitred.usecases.hub.CreateHub

class HubSetupViewModel(
    private val createHubUseCase: CreateHub
) : ViewModel() {
    private val _hubSetupModel: MutableState<HubSetupUIModel> = mutableStateOf(HubSetupUIModel())
    val hubSetupModel: State<HubSetupUIModel>
        get() = _hubSetupModel

    fun onInputEvent(fieldInputEvent: FieldInputEvent) {
        when (fieldInputEvent) {
            is FieldInputEvent.FieldFocusAcquire -> {
                _hubSetupModel.value =
                    when (fieldInputEvent.fieldName) {
                        FieldInputEvent.FieldName.HUB_ADDRESS -> _hubSetupModel.value.copy(
                            hubAddressField = _hubSetupModel.value.hubAddressField.copy(isTouched = true)
                        )

                        FieldInputEvent.FieldName.HUB_NAME -> hubSetupModel.value.copy(
                            hubNameField = _hubSetupModel.value.hubNameField.copy(isTouched = true)
                        )

                        FieldInputEvent.FieldName.PASSWORD -> hubSetupModel.value.copy(
                            hubPasswordField = _hubSetupModel.value.hubPasswordField.copy(isTouched = true)
                        )
                    }
            }

            is FieldInputEvent.FieldValueInput -> {
                _hubSetupModel.value =
                    when (fieldInputEvent.fieldName) {
                        FieldInputEvent.FieldName.HUB_ADDRESS -> _hubSetupModel.value.copy(
                            hubAddressField = _hubSetupModel.value.hubAddressField.copy(
                                value = fieldInputEvent.fieldValue,
                                hasError = _hubSetupModel.value.hubAddressField.isTouched
                                        && (fieldInputEvent.fieldValue.isBlank()
                                        || !URLUtil.isValidUrl(fieldInputEvent.fieldValue))
                            ),
                        )

                        FieldInputEvent.FieldName.HUB_NAME -> hubSetupModel.value.copy(
                            hubNameField = _hubSetupModel.value.hubNameField.copy(
                                value = fieldInputEvent.fieldValue,
                                hasError = _hubSetupModel.value.hubNameField.isTouched
                                        && (fieldInputEvent.fieldValue.isBlank()
                                        || !(5..25).contains(fieldInputEvent.fieldValue.length)
                                        || !fieldInputEvent.fieldValue.matches("^[ \\w]+\$".toRegex()))
                            )
                        )

                        FieldInputEvent.FieldName.PASSWORD -> hubSetupModel.value.copy(
                            hubPasswordField = _hubSetupModel.value.hubPasswordField.copy(
                                value = fieldInputEvent.fieldValue,
                                hasError = _hubSetupModel.value.hubPasswordField.isTouched
                                        && fieldInputEvent.fieldValue.isBlank()
                            )
                        )
                    }
                validateForm()
            }

            is FieldInputEvent.FieldTrailingButtonClick -> {
                _hubSetupModel.value =
                    when (fieldInputEvent.fieldName) {
                        FieldInputEvent.FieldName.PASSWORD -> hubSetupModel.value.copy(
                            hubPasswordField = _hubSetupModel.value.hubPasswordField.copy(
                                clearPassword = !_hubSetupModel.value.hubPasswordField.clearPassword
                            )
                        )

                        else -> hubSetupModel.value
                    }
            }
        }
    }

    private fun validateForm() {
        _hubSetupModel.value = _hubSetupModel.value.copy(
            formIsValid = (!_hubSetupModel.value.hubAddressField.hasError && _hubSetupModel.value.hubAddressField.isTouched)
                    && (!_hubSetupModel.value.hubNameField.hasError && _hubSetupModel.value.hubNameField.isTouched)
                    && (!_hubSetupModel.value.hubPasswordField.hasError && _hubSetupModel.value.hubPasswordField.isTouched)
        )
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val homeKitRedContainer =
                    (this[APPLICATION_KEY] as HomeKitRedApp).homeKitRedContainer
                val createHubUseCase = CreateHub(
                    hubAPIRepository = homeKitRedContainer.hubAPIRepository
                )
                HubSetupViewModel(
                    createHubUseCase = createHubUseCase
                )
            }
        }
    }
}