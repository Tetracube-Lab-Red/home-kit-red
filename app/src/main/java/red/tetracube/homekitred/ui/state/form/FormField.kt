package red.tetracube.homekitred.ui.state.form

import androidx.compose.runtime.State

interface FormField {

    val isValid: State<Boolean>
    val value: State<String>
    val isDirty: State<Boolean>
    val message: State<String?>

    fun hasError(): Boolean

    fun setValue(value: String)

}