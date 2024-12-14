package red.tetracube.homekitred.ui.form

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun rememberTextField(
    validationFn: ((String) -> Pair<Boolean, String>)?
): TextField {
    return remember {
        TextField(validationFn)
    }
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
fun rememberSelectField(
    validationFn: ((String) -> Pair<Boolean, String>)?
): SelectField {
    return remember {
        SelectField(validationFn)
    }
}