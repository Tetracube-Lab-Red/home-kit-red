package red.tetracube.homekitred.ui.state.form

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State


@Stable
class PasswordField internal constructor(
    private val validationFn: ((String) -> Pair<Boolean, String>)?
) : FormField {

    private val _isValid = mutableStateOf(false)
    override val isValid
        get() = _isValid

    private val _value = mutableStateOf("")
    override val value
        get() = _value

    private val _isDirty = mutableStateOf(false)
    override val isDirty
        get() = _isDirty

    private val _message = mutableStateOf<String?>(null)
    override val message
        get() = _message

    private val _showPassword = mutableStateOf(false)
    val showPassword: State<Boolean>
        get() = _showPassword

    override fun hasError() = isDirty.value && !isValid.value

    override fun setValue(value: String) {
        this.value.value = value
        this.isDirty.value = true
        validateInput()
    }

    fun togglePasswordVisibility() {
        _showPassword.value = !showPassword.value
    }

    private fun validateInput() {
        validationFn?.invoke(value.value)
            ?.let { validationOut ->
                val (error, supportingText) = validationOut
                _isValid.value = error
                _message.value = supportingText
            }
    }

}