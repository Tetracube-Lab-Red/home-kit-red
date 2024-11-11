package red.tetracube.homekitred.iot.device.provisioning

import red.tetracube.homekitred.data.enumerations.DeviceType

class FormValidationUseCase {

    val hostValidationPattern =
        "(?<href>(?<origin>(?<protocol>(?<scheme>[a-z][\\w\\-]{2,}):)?(?:\\/\\/)?)?(?:(?<username>[^:\\s]*):(?<password>[^@\\s]*)@)?(?<host>(?<hostname>\\B\\.{1,2}\\B(?=[a-z\\d]|\\/)|(?:[a-z][a-z-]*)(?:\\.[a-z][a-z-\\.]*|(?=\\/))|(?:[^\\s\\\$]\\d[a-z\\d-]*)(?:\\.[a-z][a-z-\\.]*)|(?:(?:25[0-5]|(?:2[0-4]|1\\d|[1-9]|)\\d)(?:\\.(?!\$)|\$)){4}|localhost)(?:\\:(?<port>\\d+))?)(?<pathname>\\/[^?#\\s]*)?(?<search>\\?[^#\\s]*)?(?<hash>#[^\\s]*)?)"
            .toRegex()

    fun validateDeviceName(deviceName: String) =
        if (deviceName.isBlank()) {
            false to "Required"
        } else if (!(5..30).contains(deviceName.length)) {
            false to "Name too short"
        } else if (!deviceName.matches("^[ \\w]+\$".toRegex())) {
            false to "Name should contain only alphanumeric characters or spaces"
        } else {
            true to "✅ OK"
        }

    fun validateDeviceType(deviceType: DeviceType) =
        if (deviceType == DeviceType.NONE) {
            false to "Required"
        } else {
            true to "✅ OK"
        }

    fun validateNutHost(nutHost: String) =
        if (nutHost.isBlank()) {
            false to "Required"
        } else if (!nutHost.matches(hostValidationPattern)) {
            false to "Invalid hostname"
        } else {
            true to "✅ OK"
        }

    fun validateNutPort(nutPort: String) =
        if (nutPort.isBlank()) {
            false to "Required"
        } else if (!nutPort.matches("^\\d+\$".toRegex())) {
            false to "Invalid port"
        } else if (nutPort.toInt() < 0 || nutPort.toInt() > 65535) {
            false to "Invalid port"
        } else {
            true to "✅ OK"
        }

    fun validateNutUPSAlias(nutUPSAlias: String) =
        if (nutUPSAlias.isBlank()) {
            false to "Required"
        } else if (!(5..30).contains(nutUPSAlias.length)) {
            false to "Name too short"
        } else if (!nutUPSAlias.matches("^[-\\w]+\$".toRegex())) {
            false to "Name should contain only alphanumeric characters"
        } else {
            true to "✅ OK"
        }

}