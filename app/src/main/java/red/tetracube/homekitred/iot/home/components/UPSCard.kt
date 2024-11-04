package red.tetracube.homekitred.iot.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import red.tetracube.homekitred.R

@Composable
fun UPSCard() {
    ElevatedCard(
        modifier = Modifier.padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    modifier = Modifier
                        .clip(shape = CircleShape)
                        .background(color = MaterialTheme.colorScheme.secondaryContainer),
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    painter = painterResource(R.drawable.power_48px),
                    contentDescription = null,
                )

                Column(modifier = Modifier) {
                    Text(
                        "UPS Studio",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        "Online Battery Charge",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Column(modifier = Modifier) {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        Text(
                            buildAnnotatedString {
                                withStyle(
                                    style = MaterialTheme.typography.headlineSmall
                                        .copy(color = MaterialTheme.colorScheme.tertiary)
                                        .toSpanStyle()
                                ) {
                                    append("231v")
                                }
                                withStyle(style = MaterialTheme.typography.bodySmall.toSpanStyle()) {
                                    append("\nInput Voltage")
                                }
                            },
                            textAlign = TextAlign.Center,
                        )
                    }
                    item {
                        Text(
                            buildAnnotatedString {
                                withStyle(
                                    style = MaterialTheme.typography.headlineSmall
                                        .copy(color = MaterialTheme.colorScheme.tertiary)
                                        .toSpanStyle()
                                ) {
                                    append("220v")
                                }
                                withStyle(style = MaterialTheme.typography.bodySmall.toSpanStyle()) {
                                    append("\nOutput Voltage")
                                }
                            },
                            textAlign = TextAlign.Center,
                        )
                    }
                    item {
                        Text(
                            buildAnnotatedString {
                                withStyle(
                                    style = MaterialTheme.typography.headlineSmall
                                        .copy(color = MaterialTheme.colorScheme.tertiary)
                                        .toSpanStyle()
                                ) {
                                    append("98%")
                                }
                                withStyle(style = MaterialTheme.typography.bodySmall.toSpanStyle()) {
                                    append("\nBattery Charge")
                                }
                            },
                            textAlign = TextAlign.Center,
                        )
                    }
                    item {
                        Text(
                            buildAnnotatedString {
                                withStyle(
                                    style = MaterialTheme.typography.headlineSmall
                                        .copy(color = MaterialTheme.colorScheme.tertiary)
                                        .toSpanStyle()
                                ) {
                                    append("23%")
                                }
                                withStyle(style = MaterialTheme.typography.bodySmall.toSpanStyle()) {
                                    append("\nLoad")
                                }
                            },
                            textAlign = TextAlign.Center,
                        )
                    }

                    item {
                        Text(
                            buildAnnotatedString {
                                withStyle(
                                    style = MaterialTheme.typography.headlineSmall
                                        .copy(color = MaterialTheme.colorScheme.tertiary)
                                        .toSpanStyle()
                                ) {
                                    append("1")
                                }
                                withStyle(style = MaterialTheme.typography.bodySmall.toSpanStyle()) {
                                    append("\nNotification")
                                }
                            },
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }
    }
}