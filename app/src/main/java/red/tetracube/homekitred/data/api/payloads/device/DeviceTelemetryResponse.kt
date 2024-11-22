package red.tetracube.homekitred.data.api.payloads.device

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonSubTypes.Type
import com.fasterxml.jackson.annotation.JsonTypeInfo
import red.tetracube.homekitred.data.enumerations.ConnectivityHealth
import red.tetracube.homekitred.data.enumerations.TelemetryHealth
import red.tetracube.homekitred.data.enumerations.UPSStatus
import java.time.Instant

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "telemetryType",
    visible = true
)
@JsonSubTypes(
    Type(
        value = DeviceTelemetryResponse.UPSTelemetryData::class,
        name = "UPS_TELEMETRY_DATA"
    )
)
sealed class DeviceTelemetryResponse {

    data class UPSTelemetryData(
        val deviceSlug: String,
        val telemetryTS: Instant,
        val connectivityHealth: ConnectivityHealth,
        val telemetryHealth: TelemetryHealth,
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
        val statuses: List<UPSStatus?>,
        val telemetryType: String
    ) : DeviceTelemetryResponse()
}
