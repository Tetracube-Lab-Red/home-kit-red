package red.tetracube.homekitred.data.api.payloads.device

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonSubTypes.Type
import com.fasterxml.jackson.annotation.JsonTypeInfo
import red.tetracube.homekitred.data.enumerations.ConnectivityStatus
import red.tetracube.homekitred.data.enumerations.TelemetryStatus
import red.tetracube.homekitred.data.enumerations.UPSStatus
import java.time.Instant

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "telemetryType",
    visible = true
)
@JsonSubTypes(
    JsonSubTypes.Type(
        value = DeviceTelemetryResponse.UPSTelemetryData::class,
        name = "UPS_TELEMETRY_DATA"
    )
)
abstract class DeviceTelemetryResponse(
    val slug: String,
    val timestamp: Instant,
    val connectivity: ConnectivityStatus,
    val telemetryTransmission: TelemetryStatus,
    val deviceTelemetryType: String
) {

    data class UPSTelemetryData(
        val deviceSlug: String,
        val telemetryTS: Instant,
        val connectivityStatus: ConnectivityStatus,
        val telemetryStatus: TelemetryStatus,
        val outFrequency: Float,
        val outVoltage: Float,
        val outCurrent: Float,
        val batteryVoltage: Float,
        val batteryRuntime: Long,
        val load: Long,
        val temperature: Float,
        val inFrequency: Float,
        val inVoltage: Float,
        val powerFactor: Float,
        val batteryCharge: Float,
        val statuses: List<UPSStatus>,
        val telemetryType: String
    ) : DeviceTelemetryResponse(
        deviceSlug,
        telemetryTS,
        connectivityStatus,
        telemetryStatus,
        telemetryType
    )
}
