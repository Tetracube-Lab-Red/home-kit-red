package red.tetracube.homekitred.ui.state.form

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State

@Stable
class SelectField<T> internal constructor(private val validationFn: ((T) -> Pair<Boolean, String>)?) :
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

    private val _expanded = mutableStateOf(false)
    val expanded: State<Boolean>
        get() = _expanded

    private val _option = mutableStateOf<T?>(null)
    val option: State<T?>
        get() = _option

    override fun hasError() = isDirty.value && !isValid.value

    fun toggleSelect() {
      _expanded.value = !expanded.value
    }

    fun setOptionValue(value: T) {
        _option.value = value
    }

    override fun setValue(value: String) {
        this._value.value = value
        this._isDirty.value = true
        validateInput()
    }

    private fun validateInput() {
        validationFn?.invoke(option.value!!)
            ?.let { validationOut ->
                val (error, supportingText) = validationOut
                _isValid.value = error
                _message.value = supportingText
            }
    }

}