package red.tetracube.homekitred.hubcentral.login.models

import android.webkit.URLUtil

class FormValidationUsecase {

    fun validateHostAddress(hostAddress: String) =
        if (hostAddress.isBlank()) {
            false to "Required"
        } else if (!URLUtil.isValidUrl(hostAddress)) {
            false to "Invalid address"
        } else {
            true to "✅ OK"
        }

    fun validateHubName(hostAddress: String) =
        if (hostAddress.isBlank()) {
            false to "Required"
        } else {
            true to "✅ OK"
        }

    fun validatePassword(hostAddress: String) =
        if (hostAddress.isBlank()) {
            false to "Required"
        } else {
            true to "✅ OK"
        }

}