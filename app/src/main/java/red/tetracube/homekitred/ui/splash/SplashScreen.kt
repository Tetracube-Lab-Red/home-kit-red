package red.tetracube.homekitred.ui.splash

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import red.tetracube.homekitred.R
import red.tetracube.homekitred.navigation.Routes
import red.tetracube.homekitred.business.models.ui.UIState

@Composable
fun SplashScreen(
    navHostController: NavHostController,
    viewModel: SplashViewModel
) {
    val uiState = viewModel.uiState.value
    val hubExists = viewModel.hubActiveExists.value

    LaunchedEffect(Unit) {
        viewModel.loadDefaultHub()
    }

    LaunchedEffect(uiState, hubExists) {
        if (uiState is UIState.FinishedWithSuccess) {
            if (hubExists != null) {
                if (!hubExists) {
                    navHostController.navigate(Routes.Login) {
                        popUpTo<Routes.Splash> { inclusive = true }
                    }
                } else {
                    navHostController.navigate(Routes.IoT) {
                        popUpTo<Routes.Splash> { inclusive = true }
                    }
                }
            }
        }
    }

    SplashScreenUI()

    if (uiState is UIState.FinishedWithError<*>) {
        HubConnectionError(
            onRetryClick = {
                viewModel.loadDefaultHub()
            }
        )
    }
}

@Composable
fun SplashScreenUI() {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                        append("HomeKit")
                    }
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.tertiary)) {
                        append(" RED")
                    }
                },
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(64.dp))
            LinearProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HubConnectionError(
    onRetryClick: () -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        ElevatedCard {
            Column(
                modifier = Modifier.padding(16.dp),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Icon(
                        painter = painterResource(R.drawable.network_check_24px),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Hub connection error", style = MaterialTheme.typography.headlineSmall)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "There was an error during hub connection, check that the services are running, yor device is connected to right network",
                )
                Spacer(modifier = Modifier.height(24.dp))
                TextButton(
                    onClick = onRetryClick,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Retry")
                }
            }
        }
    }
}