package red.tetracube.homekitred.hubcentral.login.models

data class LoginUIModel(
    val hubAddressField: HubAddressField = HubAddressField(),
    val hubNameField: HubNameField = HubNameField(),
    val hubPasswordField: HubPasswordField = HubPasswordField(),
    val formIsValid: Boolean = false
)

data class HubAddressField(
    val isDirty: Boolean = false,
    val isValid: Boolean = false,
    val validationMessage: String = "Required",
    val value: String = ""
)

data class HubNameField(
    val isDirty: Boolean = false,
    val isValid: Boolean = false,
    val validationMessage: String = "Required",
    val value: String = ""
)

data class HubPasswordField(
    val isDirty: Boolean = false,
    val isValid: Boolean = false,
    val clearPassword: Boolean = false,
    val validationMessage: String = "Required",
    val value: String = ""
)
