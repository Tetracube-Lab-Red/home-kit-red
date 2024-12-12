package red.tetracube.homekitred.iot.home.domain.models

import red.tetracube.homekitred.business.enumerations.ConnectivityHealth
import red.tetracube.homekitred.business.enumerations.TelemetryHealth
import red.tetracube.homekitred.business.enumerations.UPSStatus
import java.time.Instant
import java.util.UUID

sealed class BasicTelemetry(
    val id: UUID,
    val connection: ConnectivityHealth,
    val telemetry: TelemetryHealth,
    val timestamp: Instant
) {

    class UnknownBasicTelemetry() : BasicTelemetry(
        UUID.randomUUID(),
        ConnectivityHealth.UNKNOWN,
        TelemetryHealth.UNKNOWN,
        Instant.now()
    )

    data class UPSBasicTelemetry(
        val deviceId: UUID,
        val primaryStatus: UPSStatus = UPSStatus.NULL,
        val secondaryStatus: UPSStatus? = null,
        val connectivityHealth: ConnectivityHealth = ConnectivityHealth.UNKNOWN,
        val telemetryHealth: TelemetryHealth = TelemetryHealth.UNKNOWN,
        val telemetryTS: Instant = Instant.now()
    ) : BasicTelemetry(
        deviceId,
        connectivityHealth,
        telemetryHealth,
        telemetryTS
    )

}