package red.tetracube.homekitred.iot.home.domain.models

import red.tetracube.homekitred.data.enumerations.ConnectivityHealth
import red.tetracube.homekitred.data.enumerations.TelemetryHealth
import red.tetracube.homekitred.data.enumerations.UPSStatus
import java.time.Instant

sealed class BasicTelemetry(
    val connection: ConnectivityHealth,
    val telemetry: TelemetryHealth,
    val timestamp: Instant
) {

    class UnknownBasicTelemetry() : BasicTelemetry(
        ConnectivityHealth.UNKNOWN,
        TelemetryHealth.UNKNOWN,
        Instant.now()
    )

    data class UPSBasicTelemetry(
        val primaryStatus: UPSStatus = UPSStatus.NULL,
        val secondaryStatus: UPSStatus? = null,
        val connectivityHealth: ConnectivityHealth = ConnectivityHealth.UNKNOWN,
        val telemetryHealth: TelemetryHealth = TelemetryHealth.UNKNOWN,
        val telemetryTS: Instant = Instant.now()
    ) : BasicTelemetry(
        connectivityHealth,
        telemetryHealth,
        telemetryTS
    )

}