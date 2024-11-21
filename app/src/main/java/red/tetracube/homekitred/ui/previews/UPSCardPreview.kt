package red.tetracube.homekitred.ui.previews

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import red.tetracube.homekitred.data.enumerations.DeviceType
import red.tetracube.homekitred.iot.home.components.UPSCard
import red.tetracube.homekitred.iot.home.domain.models.Device
import red.tetracube.homekitred.ui.theme.HomeKitRedTheme
import java.time.Instant

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun UPSCardPreview() {
    HomeKitRedTheme {
        UPSCard(
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
    }
}