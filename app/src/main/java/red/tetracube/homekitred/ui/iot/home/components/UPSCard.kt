package red.tetracube.homekitred.ui.iot.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import red.tetracube.homekitred.ui.iot.home.models.BasicTelemetry
import red.tetracube.homekitred.ui.iot.home.models.BasicTelemetry.UPSBasicTelemetry
import red.tetracube.homekitred.ui.iot.home.models.Device
import red.tetracube.homekitred.ui.theme.AppTypography
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun UPSCard(
    device: Device,
    basicTelemetry: BasicTelemetry?,
    onItemClick: () -> Unit,
) {
    val lightLabel =
        AppTypography.labelMedium.copy(color = MaterialTheme.colorScheme.onBackground)
    val boldBody = AppTypography.bodyMedium.copy(fontWeight = FontWeight.Bold)
    ElevatedCard(
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 8.dp)
            .clickable {
                onItemClick()
            }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(device.name, style = MaterialTheme.typography.titleMedium)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Status", style = lightLabel)
                    (device.telemetry as? UPSBasicTelemetry)?.let { telemetry ->
                        Text(
                            "${telemetry.primaryStatus}\n${telemetry.secondaryStatus ?: ""}",
                            style = boldBody,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Notifications", style = lightLabel)
                    (device.telemetry as? UPSBasicTelemetry)?.let { telemetry ->
                        Text(
                            "0",
                            style = boldBody,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Connectivity", style = lightLabel)
                    basicTelemetry?.let {
                        Text(
                            "${basicTelemetry.connectivityHealth}\n${basicTelemetry.telemetryHealth}",
                            style = boldBody,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text("Last update: ", style = lightLabel)
                basicTelemetry?.let {
                    Text(
                        DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
                            .withZone(ZoneId.systemDefault())
                            .format(basicTelemetry.telemetryTS),
                        style = boldBody
                    )
                }
            }
        }
    }
}