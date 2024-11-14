package red.tetracube.homekitred.ui.previews

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import red.tetracube.homekitred.iot.home.components.UPSCard
import red.tetracube.homekitred.ui.theme.HomeKitRedTheme

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun UPSCardPreview() {
    HomeKitRedTheme {
        UPSCard()
    }
}