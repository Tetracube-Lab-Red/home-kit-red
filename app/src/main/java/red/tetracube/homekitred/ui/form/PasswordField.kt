package red.tetracube.homekitred.ui.form

import androidx.compose.runtime.Stable


@Stable
class PasswordField internal constructor(
    private val validationFn: ((String) -> Pair<Boolean, String>)?
) : FormField {

    override var isValid = false
        private set
    override var value = ""
        private set
    override var isDirty = false
        private set
    override var message = null as String?
        private set
    var showPassword = false
        private set

    override fun getSupportingMessage(): String? = message

    override fun hasError() = isDirty && !isValid

    override fun setValue(value: String) {
        this.value = value
        this.isDirty = true
        validateInput()
    }

    fun togglePasswordVisibility() {
        showPassword = !showPassword
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