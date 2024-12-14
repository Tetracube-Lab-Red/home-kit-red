package red.tetracube.homekitred.ui.hub.login

import android.webkit.URLUtil


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
    } else {
        true to "✅ OK"
    }

fun validatePassword(password: String) =
    if (password.isBlank()) {
        false to "Required"
    } else {
        true to "✅ OK"
    }
