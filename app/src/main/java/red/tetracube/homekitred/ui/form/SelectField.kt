package red.tetracube.homekitred.ui.form

import androidx.compose.runtime.Stable

@Stable
class SelectField internal constructor(private val validationFn: ((String) -> Pair<Boolean, String>)?) :
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
    var option: String? = null
        private set

    override fun getSupportingMessage(): String? = message

    override fun hasError() = isDirty && !isValid

    fun toggleSelect() = !expanded

    fun setOptionValue(value: String) {
        option = value
    }

    override fun setValue(value: String) {
        this.value = value
        this.isDirty = true
        validateInput()
    }

    private fun validateInput() {
        validationFn?.invoke(value)
            ?.let { validationOut ->
                val (error, supportingText) = validationOut
                isValid = error
                message = supportingText
            }
    }

}