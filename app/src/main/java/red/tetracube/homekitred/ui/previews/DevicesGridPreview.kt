package red.tetracube.homekitred.ui.previews

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import red.tetracube.homekitred.data.enumerations.DeviceType
import red.tetracube.homekitred.iot.home.DevicesGrid
import red.tetracube.homekitred.iot.home.domain.models.Device
import java.time.Instant

@Preview
@Composable
fun DevicesGridPreview() {
    DevicesGrid(
        1,
        listOf(
            Device(
                "slug",
                "UPS device",
                "room",
                "Living Room",
                "Online Battery Charge",
                3,
                "Online - Transmitting",
                DeviceType.UPS,
                Instant.now().toString()
            )
        )
    )
}