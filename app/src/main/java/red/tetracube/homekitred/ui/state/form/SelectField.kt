package red.tetracube.homekitred.ui.state.form

import androidx.compose.runtime.Stable

@Stable
class SelectField<T> internal constructor(private val validationFn: ((T) -> Pair<Boolean, String>)?) :
    FormField {

    override var isValid = false
        private set
    override var value = ""
        private set
    override var isDirty = false
        private set
    override var message = null as String?
        private set
    var expanded = false
        private set
    var option: T? = null
        private set

    override fun getSupportingMessage(): String? = message

    override fun hasError() = isDirty && !isValid

    fun toggleSelect() = !expanded

    fun setOptionValue(value: T) {
        option = value
    }

    override fun setValue(value: String) {
        this.value = value
        this.isDirty = true
        validateInput()
    }

    private fun validateInput() {
        validationFn?.invoke(option!!)
            ?.let { validationOut ->
                val (error, supportingText) = validationOut
                isValid = error
                message = supportingText
            }
    }

}