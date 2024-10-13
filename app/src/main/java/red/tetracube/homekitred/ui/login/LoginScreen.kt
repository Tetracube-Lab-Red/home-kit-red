package red.tetracube.homekitred.ui.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import red.tetracube.homekitred.R

@Composable
fun LoginScreen(
    navHostController: NavHostController,
    loginViewModel: LoginViewModel
) {
    LoginScreenUI()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreenUI() {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Sign in", color = MaterialTheme.colorScheme.tertiary)
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.fillMaxWidth(0.1f)) {
                    Icon(painter = painterResource(R.drawable.hub_24px), null)
                }
                Column {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Hub name") },
                        value = "hello",
                        onValueChange = { value: String -> },
                        singleLine = true,
                        maxLines = 1
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.fillMaxWidth(0.1f)) {
                    Icon(painter = painterResource(R.drawable.password_24px), null)
                }
                Column {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Hub name") },
                        value = "hello",
                        onValueChange = { value: String -> },
                        singleLine = true,
                        maxLines = 1
                    )
                }
            }
        }
    }
}
