package red.tetracube.homekitred.ui.state.form

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf

@Stable
class TextField internal constructor(private val validationFn: ((String) -> Pair<Boolean, String>)?) :
    FormField {

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

    override fun hasError() = isDirty.value && !isValid.value

    override fun setValue(value: String) {
        this.value.value = value
        this.isDirty.value = true
        validateInput()
    }

    private fun validateInput() {
        validationFn?.invoke(value.value)
            ?.let { validationOut ->
                val (error, supportingText) = validationOut
                isValid.value = error
                message.value = supportingText
            }
    }

}