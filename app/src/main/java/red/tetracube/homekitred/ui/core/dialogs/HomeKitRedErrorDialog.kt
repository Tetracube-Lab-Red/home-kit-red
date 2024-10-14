package red.tetracube.homekitred.ui.core.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import red.tetracube.homekitred.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeKitRedErrorDialog(
    homeKitRedError: String,
    navHostController: NavHostController
) {
    BasicAlertDialog(
        onDismissRequest = {
            navHostController.popBackStack()
        },
        modifier = Modifier,
        properties = DialogProperties ()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(painter = painterResource(R.drawable.error_24px), contentDescription = null)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    homeKitRedError,
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Text("Something about the error")
        }
    }
}