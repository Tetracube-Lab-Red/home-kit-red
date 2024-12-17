package red.tetracube.homekitred.ui.iot.home.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import red.tetracube.homekitred.business.enumerations.ConnectivityHealth
import red.tetracube.homekitred.business.enumerations.DeviceType
import red.tetracube.homekitred.business.enumerations.TelemetryHealth
import red.tetracube.homekitred.business.enumerations.UPSStatus
import java.time.Instant
import java.util.UUID

data class IoTDashboardModel(
    val hubId: UUID,
    val hubName: String,
    val avatarName: String,
    val rooms: List<Room>,
    val devices: List<Device>
)

data class Room(
    val id: UUID?,
    val name: String
)

@Parcelize
data class Device(
    val id: UUID,
    val name: String,
    val roomName: String?,
    val roomId: UUID?,
    val notifications: Int,
    val type: DeviceType,
    val telemetry: BasicTelemetry?
) : Parcelable

@Parcelize
sealed class BasicTelemetry : Parcelable {

    abstract val id: UUID
    abstract val deviceId: UUID
    abstract val connectivityHealth: ConnectivityHealth
    abstract val telemetryHealth: TelemetryHealth
    abstract val telemetryTS: Instant

    @Parcelize
    data class UPSBasicTelemetry(
        override val id: UUID,
        override val deviceId: UUID,
        val primaryStatus: UPSStatus,
        val secondaryStatus: UPSStatus?,
        override val connectivityHealth: ConnectivityHealth,
        override val telemetryHealth: TelemetryHealth,
        override val telemetryTS: Instant
    ) : BasicTelemetry()

}