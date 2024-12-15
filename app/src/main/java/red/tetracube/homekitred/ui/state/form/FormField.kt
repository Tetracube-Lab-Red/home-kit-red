package red.tetracube.homekitred.ui.state.form

interface FormField {

    val isValid: Boolean
    val value: String
    val isDirty: Boolean
    val message: String?

    fun getSupportingMessage(): String?

    fun hasError(): Boolean

    fun setValue(value: String)

}