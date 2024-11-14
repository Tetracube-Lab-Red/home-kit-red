package red.tetracube.homekitred.ui.previews

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import red.tetracube.homekitred.splash.HubConnectionError

@Preview()
@Composable
fun HubConnectionErrorPreview() {
    HubConnectionError(
        onRetryClick = {}
    )
}