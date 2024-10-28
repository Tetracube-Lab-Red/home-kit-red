package red.tetracube.homekitred.hubcentral.login

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
import red.tetracube.homekitred.data.services.HubLocalDataService
import red.tetracube.homekitred.hubcentral.login.models.FieldInputEvent
import red.tetracube.homekitred.hubcentral.login.models.LoginUIModel
import red.tetracube.homekitred.ui.core.models.UIState

class LoginViewModel(
    private val loginUseCases: LoginUseCases
) : ViewModel() {

    val formValidationUseCase = FormValidationUseCase()

    private val _loginUIModel: MutableState<LoginUIModel> = mutableStateOf(LoginUIModel())
    val loginUIModel: State<LoginUIModel>
        get() = _loginUIModel

    private val _uiState = mutableStateOf<UIState>(UIState.Neutral)
    val uiState: State<UIState>
        get() = _uiState

    fun onInputEvent(fieldInputEvent: FieldInputEvent) {
        when (fieldInputEvent) {
            is FieldInputEvent.FieldValueInput -> {
                _loginUIModel.value =
                    when (fieldInputEvent.fieldName) {
                        FieldInputEvent.FieldName.HUB_ADDRESS -> _loginUIModel.value.copy(
                            hubAddressField = _loginUIModel.value.hubAddressField.copy(
                                value = fieldInputEvent.fieldValue,
                                isDirty = true
                            ),
                        )

                        FieldInputEvent.FieldName.HUB_NAME -> loginUIModel.value.copy(
                            hubNameField = _loginUIModel.value.hubNameField.copy(
                                value = fieldInputEvent.fieldValue,
                                isDirty = true
                            )
                        )

                        FieldInputEvent.FieldName.PASSWORD -> loginUIModel.value.copy(
                            hubPasswordField = _loginUIModel.value.hubPasswordField.copy(
                                value = fieldInputEvent.fieldValue,
                                isDirty = true
                            )
                        )
                    }
                validateForm()
            }

            is FieldInputEvent.FieldTrailingButtonClick -> {
                _loginUIModel.value =
                    when (fieldInputEvent.fieldName) {
                        FieldInputEvent.FieldName.PASSWORD -> loginUIModel.value.copy(
                            hubPasswordField = _loginUIModel.value.hubPasswordField.copy(
                                clearPassword = !_loginUIModel.value.hubPasswordField.clearPassword
                            )
                        )

                        else -> loginUIModel.value
                    }
            }
        }
    }

    private fun validateForm() {
        if (_loginUIModel.value.hubAddressField.isDirty) {
            val (isValid, message) = formValidationUseCase.validateHostAddress(_loginUIModel.value.hubAddressField.value)
            _loginUIModel.value = _loginUIModel.value.copy(
                hubAddressField = _loginUIModel.value.hubAddressField.copy(
                    isValid = isValid,
                    validationMessage = message
                )
            )
        }
        if (_loginUIModel.value.hubNameField.isDirty) {
            val (isValid, message) = formValidationUseCase.validateHubName(_loginUIModel.value.hubNameField.value)
            _loginUIModel.value = _loginUIModel.value.copy(
                hubNameField = _loginUIModel.value.hubNameField.copy(
                    isValid = isValid,
                    validationMessage = message
                )
            )
        }
        if (_loginUIModel.value.hubPasswordField.isDirty) {
            val (isValid, message) = formValidationUseCase.validatePassword(_loginUIModel.value.hubPasswordField.value)
            _loginUIModel.value = _loginUIModel.value.copy(
                hubPasswordField = _loginUIModel.value.hubPasswordField.copy(
                    isValid = isValid,
                    validationMessage = message
                )
            )
        }
        _loginUIModel.value = _loginUIModel.value.copy(
            formIsValid = (_loginUIModel.value.hubAddressField.isValid && _loginUIModel.value.hubAddressField.isDirty)
                    && (_loginUIModel.value.hubNameField.isValid && _loginUIModel.value.hubNameField.isDirty)
                    && (_loginUIModel.value.hubPasswordField.isValid && _loginUIModel.value.hubPasswordField.isDirty)
        )
    }

    fun onLoginButtonClick() {
        _uiState.value = UIState.Loading
        viewModelScope.launch {
            val createHubResult = loginUseCases.login(
                hubAddress = _loginUIModel.value.hubAddressField.value,
                hubName = _loginUIModel.value.hubNameField.value,
                hubPassword = _loginUIModel.value.hubPasswordField.value
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
                val loginUseCases = LoginUseCases(
                    homeKitRedContainer.hubAPIRepository,
                    homeKitRedContainer.homeKitRedDatabase.hubRepository(),
                    hubLocalDataService = HubLocalDataService(
                        roomAPIRepository = homeKitRedContainer.roomAPIRepository,
                        roomDatasource = homeKitRedContainer.homeKitRedDatabase.roomRepository(),
                    )
                )
                LoginViewModel(loginUseCases)
            }
        }
    }

}