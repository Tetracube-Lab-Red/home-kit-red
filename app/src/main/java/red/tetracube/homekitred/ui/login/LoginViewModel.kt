package red.tetracube.homekitred.ui.login

import android.webkit.URLUtil
import androidx.compose.runtime.State
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import red.tetracube.homekitred.ui.login.models.FieldInputEvent
import red.tetracube.homekitred.ui.login.models.LoginUIModel

class LoginViewModel : ViewModel() {

    private val _loginUIModel: MutableState<LoginUIModel> = mutableStateOf(LoginUIModel())
    val loginUIModel: State<LoginUIModel>
        get() = _loginUIModel

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
                                        && (fieldInputEvent.fieldValue.isEmpty()
                                        || !URLUtil.isValidUrl(fieldInputEvent.fieldValue))
                            ),
                        )

                        FieldInputEvent.FieldName.HUB_NAME -> loginUIModel.value.copy(
                            hubNameField = _loginUIModel.value.hubNameField.copy(
                                value = fieldInputEvent.fieldValue,
                                hasError = _loginUIModel.value.hubNameField.isTouched
                                        && fieldInputEvent.fieldValue.isEmpty()
                            )
                        )

                        FieldInputEvent.FieldName.PASSWORD -> loginUIModel.value.copy(
                            hubPasswordField = _loginUIModel.value.hubPasswordField.copy(
                                value = fieldInputEvent.fieldValue,
                                hasError = _loginUIModel.value.hubPasswordField.isTouched
                                        && fieldInputEvent.fieldValue.isEmpty()
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

}