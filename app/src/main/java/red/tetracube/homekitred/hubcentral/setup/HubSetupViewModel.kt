package red.tetracube.homekitred.hubcentral.setup

import android.webkit.URLUtil
import androidx.compose.runtime.MutableState
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
import red.tetracube.homekitred.hubcentral.setup.models.HubSetupUIModel
import red.tetracube.homekitred.ui.core.models.UIState
import red.tetracube.homekitred.hubcentral.login.models.FieldInputEvent

class HubSetupViewModel(
    private val hubSetupUseCases: HubSetupUseCases
) : ViewModel() {
    private val formValidationUseCase = FormValidationUseCase()

    private val _hubSetupModel: MutableState<HubSetupUIModel> = mutableStateOf(HubSetupUIModel())
    val hubSetupModel: State<HubSetupUIModel>
        get() = _hubSetupModel

    private val _uiState = mutableStateOf<UIState>(UIState.Neutral)
    val uiState: State<UIState>
        get() = _uiState

    fun onInputEvent(fieldInputEvent: FieldInputEvent) {
        when (fieldInputEvent) {
            is FieldInputEvent.FieldValueInput -> {
                _hubSetupModel.value =
                    when (fieldInputEvent.fieldName) {
                        FieldInputEvent.FieldName.HUB_ADDRESS -> _hubSetupModel.value.copy(
                            hubAddressField = _hubSetupModel.value.hubAddressField.copy(
                                value = fieldInputEvent.fieldValue,
                                isDirty = true
                            ),
                        )

                        FieldInputEvent.FieldName.HUB_NAME -> hubSetupModel.value.copy(
                            hubNameField = _hubSetupModel.value.hubNameField.copy(
                                value = fieldInputEvent.fieldValue,
                                isDirty = true
                            )
                        )

                        FieldInputEvent.FieldName.PASSWORD -> hubSetupModel.value.copy(
                            hubPasswordField = _hubSetupModel.value.hubPasswordField.copy(
                                value = fieldInputEvent.fieldValue,
                                isDirty = true
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
        if (_hubSetupModel.value.hubAddressField.isDirty) {
            val (isValid, message) = formValidationUseCase.validateHostAddress(_hubSetupModel.value.hubAddressField.value)
            _hubSetupModel.value = _hubSetupModel.value.copy(
                hubAddressField = _hubSetupModel.value.hubAddressField.copy(
                    isValid = isValid,
                    validationMessage = message
                )
            )
        }
        if (_hubSetupModel.value.hubNameField.isDirty) {
            val (isValid, message) = formValidationUseCase.validateHubName(_hubSetupModel.value.hubNameField.value)
            _hubSetupModel.value = _hubSetupModel.value.copy(
                hubNameField = _hubSetupModel.value.hubNameField.copy(
                    isValid = isValid,
                    validationMessage = message
                )
            )
        }
        if (_hubSetupModel.value.hubPasswordField.isDirty) {
            val (isValid, message) = formValidationUseCase.validatePassword(_hubSetupModel.value.hubPasswordField.value)
            _hubSetupModel.value = _hubSetupModel.value.copy(
                hubPasswordField = _hubSetupModel.value.hubPasswordField.copy(
                    isValid = isValid,
                    validationMessage = message
                )
            )
        }

        _hubSetupModel.value = _hubSetupModel.value.copy(
            formIsValid = (_hubSetupModel.value.hubAddressField.isValid && _hubSetupModel.value.hubAddressField.isDirty)
                    && (_hubSetupModel.value.hubNameField.isValid && _hubSetupModel.value.hubNameField.isDirty)
                    && (_hubSetupModel.value.hubPasswordField.isValid && _hubSetupModel.value.hubPasswordField.isDirty)
        )
    }

    fun onSetupButtonClick() {
        _uiState.value = UIState.Loading
        viewModelScope.launch {
            val createHubResult = hubSetupUseCases.create(
                hubAddress = _hubSetupModel.value.hubAddressField.value,
                hubName = _hubSetupModel.value.hubNameField.value,
                hubPassword = _hubSetupModel.value.hubPasswordField.value
            )
            if (createHubResult.isFailure) {
                _uiState.value = createHubResult.exceptionOrNull()
                    ?.let { it as HomeKitRedError }
                    ?.let { UIState.FinishedWithError(it) }
                    ?: UIState.FinishedWithError(HomeKitRedError.GenericError)
            } else {
                _uiState.value = UIState.FinishedWithSuccess
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val homeKitRedContainer =
                    (this[APPLICATION_KEY] as HomeKitRedApp).homeKitRedContainer
                val createHubUseCase = HubSetupUseCases(
                    hubAPIRepository = homeKitRedContainer.hubAPIRepository
                )
                HubSetupViewModel(
                    hubSetupUseCases = createHubUseCase
                )
            }
        }
    }
}