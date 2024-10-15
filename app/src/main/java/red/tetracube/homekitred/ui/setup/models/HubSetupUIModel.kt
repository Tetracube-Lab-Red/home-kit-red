package red.tetracube.homekitred.ui.setup.models

data class HubSetupUIModel(
    val hubAddressField: HubAddressField = HubAddressField(),
    val hubNameField: HubNameField = HubNameField(),
    val hubPasswordField: HubPasswordField = HubPasswordField(),
    val formIsValid: Boolean = false
)

data class HubAddressField(
    val isTouched: Boolean = false,
    val hasError: Boolean = false,
    val value: String = ""
)

data class HubNameField(
    val isTouched: Boolean = false,
    val hasError: Boolean = false,
    val value: String = ""
)

data class HubPasswordField(
    val isTouched: Boolean = false,
    val hasError: Boolean = false,
    val clearPassword: Boolean = false,
    val value: String = ""
)
