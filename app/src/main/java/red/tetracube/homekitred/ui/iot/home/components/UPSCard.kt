package red.tetracube.homekitred.ui.iot.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import red.tetracube.homekitred.R
import red.tetracube.homekitred.ui.iot.home.models.BasicTelemetry
import red.tetracube.homekitred.ui.iot.home.models.BasicTelemetry.UPSBasicTelemetry
import red.tetracube.homekitred.ui.iot.home.models.Device
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun UPSCard(
    device: Device,
    basicTelemetry: BasicTelemetry?,
    onItemClick: () -> Unit,
) {
    ElevatedCard(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .clickable {
                onItemClick()
            }
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                tint = MaterialTheme.colorScheme.onSurface,
                painter = painterResource(R.drawable.power_48px),
                contentDescription = null,
            )
        }

        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle()) {
                        append(device.name)
                    }

                    device.roomName
                        ?.let { roomName ->
                            withStyle(
                                style = MaterialTheme.typography.titleSmall
                                    .copy(color = MaterialTheme.colorScheme.onSurface)
                                    .toSpanStyle()
                            ) {
                                append(" $roomName")
                            }
                        }
                },
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.info_24px),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary
                )
                Spacer(modifier = Modifier.width(16.dp))
                if (basicTelemetry is UPSBasicTelemetry) {
                    Text(
                        "${basicTelemetry.primaryStatus} ${basicTelemetry.secondaryStatus ?: ""}",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(R.drawable.notifications_24px),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary
                )
                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle()) {
                            append(device.notifications.toString())
                        }
                        withStyle(
                            style = MaterialTheme.typography.labelLarge.toSpanStyle()
                        ) {
                            append(" notifications")
                        }
                    },
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.settings_ethernet_24px),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary
                )
                Spacer(modifier = Modifier.width(16.dp))

                basicTelemetry?.let {
                    Text(
                        buildAnnotatedString {
                            withStyle(style = SpanStyle()) {
                                append("${basicTelemetry.connectivityHealth} - ${basicTelemetry.telemetryHealth}")
                            }
                        },
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.schedule_24px),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary
                )
                Spacer(modifier = Modifier.width(16.dp))

                basicTelemetry?.let {
                    Text(
                        buildAnnotatedString {
                            withStyle(style = SpanStyle()) {
                                append(
                                    DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss")
                                        .withZone(ZoneId.systemDefault())
                                        .format(basicTelemetry.telemetryTS)
                                )
                            }
                        },
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
        }
    }
}