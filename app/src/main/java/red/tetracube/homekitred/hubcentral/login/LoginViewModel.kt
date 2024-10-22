package red.tetracube.homekitred.hubcentral.login

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
import red.tetracube.homekitred.domain.HomeKitRedError
import red.tetracube.homekitred.ui.core.models.UIState
import red.tetracube.homekitred.hubcentral.login.models.FieldInputEvent
import red.tetracube.homekitred.hubcentral.login.models.LoginUIModel

class LoginViewModel(
    private val loginUseCases: LoginUseCases
) : ViewModel() {

    private val _loginUIModel: MutableState<LoginUIModel> = mutableStateOf(LoginUIModel())
    val loginUIModel: State<LoginUIModel>
        get() = _loginUIModel

    private val _uiState = mutableStateOf<UIState>(UIState.Neutral)
    val uiState: State<UIState>
        get() = _uiState

    fun onInputEvent(fieldInputEvent: FieldInputEvent) {
        when (fieldInputEvent) {
            is FieldInputEvent.FieldFocusAcquire -> {
                _loginUIModel.value =
                    when (fieldInputEvent.fieldName) {
                        FieldInputEvent.FieldName.HUB_ADDRESS -> _loginUIModel.value.copy(
                            hubAddressField = _loginUIModel.value.hubAddressField.copy(isTouched = true)
                        )

                        FieldInputEvent.FieldName.HUB_NAME -> loginUIModel.value.copy(
                            hubNameField = _loginUIModel.value.hubNameField.copy(isTouched = true)
                        )

                        FieldInputEvent.FieldName.PASSWORD -> loginUIModel.value.copy(
                            hubPasswordField = _loginUIModel.value.hubPasswordField.copy(isTouched = true)
                        )
                    }
            }

            is FieldInputEvent.FieldValueInput -> {
                _loginUIModel.value =
                    when (fieldInputEvent.fieldName) {
                        FieldInputEvent.FieldName.HUB_ADDRESS -> _loginUIModel.value.copy(
                            hubAddressField = _loginUIModel.value.hubAddressField.copy(
                                value = fieldInputEvent.fieldValue,
                                hasError = _loginUIModel.value.hubAddressField.isTouched
                                        && (fieldInputEvent.fieldValue.isBlank()
                                        || !URLUtil.isValidUrl(fieldInputEvent.fieldValue))
                            ),
                        )

                        FieldInputEvent.FieldName.HUB_NAME -> loginUIModel.value.copy(
                            hubNameField = _loginUIModel.value.hubNameField.copy(
                                value = fieldInputEvent.fieldValue,
                                hasError = _loginUIModel.value.hubNameField.isTouched
                                        && fieldInputEvent.fieldValue.isBlank()
                            )
                        )

                        FieldInputEvent.FieldName.PASSWORD -> loginUIModel.value.copy(
                            hubPasswordField = _loginUIModel.value.hubPasswordField.copy(
                                value = fieldInputEvent.fieldValue,
                                hasError = _loginUIModel.value.hubPasswordField.isTouched
                                        && fieldInputEvent.fieldValue.isBlank()
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
        _loginUIModel.value = _loginUIModel.value.copy(
            formIsValid = (!_loginUIModel.value.hubAddressField.hasError && _loginUIModel.value.hubAddressField.isTouched)
                    && (!_loginUIModel.value.hubNameField.hasError && _loginUIModel.value.hubNameField.isTouched)
                    && (!_loginUIModel.value.hubPasswordField.hasError && _loginUIModel.value.hubPasswordField.isTouched)
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
                    homeKitRedContainer.homeKitRedDatabase.hubRepository()
                )
                LoginViewModel(loginUseCases)
            }
        }
    }

}