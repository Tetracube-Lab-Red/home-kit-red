package red.tetracube.homekitred.ui.splash

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import red.tetracube.homekitred.app.behaviour.routing.Routes
import red.tetracube.homekitred.ui.core.models.UIState

@Composable
fun SplashScreen(
    navHostController: NavHostController,
    viewModel: SplashViewModel
) {
    val uiState = viewModel.uiState.value
    val hubExists = viewModel.hubActiveExists.value
    LaunchedEffect(Unit) {
        viewModel.checkDefaultHub()
    }
    LaunchedEffect(uiState, hubExists) {
        if (uiState is UIState.FinishedWithSuccess) {
            if (hubExists != null) {
                if (!hubExists) {
                    navHostController.navigate(Routes.Login) {
                        popUpTo<Routes.Splash>() { inclusive = true }
                    }
                } else {
                    navHostController.navigate(Routes.IoT) {
                        popUpTo<Routes.Splash>() { inclusive = true }
                    }
                }
            }
        }
    }
    SplashScreenUI()
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