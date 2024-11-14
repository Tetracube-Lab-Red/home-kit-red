package red.tetracube.homekitred.iot.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import red.tetracube.homekitred.R

@Composable
fun UPSCard() {
    OutlinedCard(
        modifier = Modifier
            .padding(8.dp)
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        Row {
            Column(
                modifier = Modifier.wrapContentWidth().width(64.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                Box(
                    modifier = Modifier
                        .clip(shape = CardDefaults.shape)
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colorScheme.tertiaryContainer)
                        .height(160.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        tint = MaterialTheme.colorScheme.onTertiaryContainer,
                        painter = painterResource(R.drawable.power_48px),
                        contentDescription = null,
                    )
                }
            }

            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.secondary)) {
                            append("UPS Studio")
                        }

                        withStyle(
                            style = MaterialTheme.typography.titleSmall
                                .copy(color = MaterialTheme.colorScheme.onSurface)
                                .toSpanStyle()
                        ) {
                            append(" Living room")
                        }
                    },
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.tertiary,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.info_24px),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.secondary)) {
                                append("Online Battery Charge")
                            }
                        },
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    // horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.notifications_24px),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.secondary)) {
                                append("0")
                            }
                            withStyle(
                                style = MaterialTheme.typography.labelLarge.toSpanStyle()
                                    .copy(color = MaterialTheme.colorScheme.onSurface)
                            ) {
                                append(" notifications")
                            }
                        },
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.error
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
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.secondary)) {
                                append("Online - Transmitting")
                            }
                        },
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }

            }
        }
    }
}