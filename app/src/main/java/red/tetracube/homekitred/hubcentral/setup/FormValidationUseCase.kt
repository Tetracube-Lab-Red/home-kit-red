package red.tetracube.homekitred.hubcentral.setup

import android.webkit.URLUtil

class FormValidationUseCase {

    fun validateHostAddress(hostAddress: String) =
        if (hostAddress.isBlank()) {
            false to "Required"
        } else if (!URLUtil.isValidUrl(hostAddress)) {
            false to "Invalid address"
        } else {
            true to "✅ OK"
        }

    fun validateHubName(name: String) =
        if (name.isBlank()) {
            false to "Required"
        } else if (!(5..25).contains(name.length)) {
            false to "Too short name"
        } else if (!name.matches("^[ \\w]+\$".toRegex())) {
            false to "Name should only alphanumeric characters"
        } else {
            true to "✅ OK"
        }

    fun validatePassword(password: String) =
        if (password.isBlank()) {
            false to "Required"
        } else if (!(5..25).contains(password.length)) {
            false to "Password is too short"
        } else {
            true to "✅ OK"
        }

}