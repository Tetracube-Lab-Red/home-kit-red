package red.tetracube.homekitred.ui.state.form

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember

@Stable
class Form internal constructor(private val fields: List<FormField>) {

    val isValid: Boolean
        get() = fields.all { it.isValid }

}

@Composable
fun rememberFormState(
    fields: List<FormField>
): Form {
    return remember {
        Form(fields)
    }
}