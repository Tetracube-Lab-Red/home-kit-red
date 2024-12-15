package red.tetracube.homekitred.ui.state.form

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun rememberTextField(
    validationFn: ((String) -> Pair<Boolean, String>)?
): TextField {
    val textField = remember {
        TextField(validationFn)
    }
    return textField
}

@Composable
fun rememberPasswordField(
    validationFn: ((String) -> Pair<Boolean, String>)?
): PasswordField {
    return remember {
        PasswordField(validationFn)
    }
}

@Composable
fun <T> rememberSelectField(
    validationFn: ((T) -> Pair<Boolean, String>)?
): SelectField<T> {
    return remember {
        SelectField(validationFn)
    }
}