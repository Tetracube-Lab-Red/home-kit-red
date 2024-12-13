package red.tetracube.homekitred.ui.hub.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.launch
import red.tetracube.homekitred.HomeKitRedApp
import red.tetracube.homekitred.business.models.errors.HomeKitRedError
import red.tetracube.homekitred.business.models.ui.UIState
import red.tetracube.homekitred.hubcentral.login.LoginUseCases

class LoginViewModel(
    private val loginUseCases: LoginUseCases
) : ViewModel() {

    private val _uiState = mutableStateOf<UIState>(UIState.Neutral)
    val uiState: State<UIState>
        get() = _uiState

    fun onLoginButtonClick(hubAddress: String, hubName: String, hubPassword: String) {
        _uiState.value = UIState.Loading
        viewModelScope.launch {
            val loginResult = loginUseCases.login(hubAddress, hubName, hubPassword)
            if (loginResult.isFailure) {
                _uiState.value = loginResult.exceptionOrNull()
                    ?.let { it as HomeKitRedError }
                    ?.let { UIState.FinishedWithError(it) }
                    ?: UIState.FinishedWithError(HomeKitRedError.GenericError)
            } else {
                _uiState.value = UIState.FinishedWithSuccess
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val homeKitRedContainer =
                    (this[APPLICATION_KEY] as HomeKitRedApp).homeKitRedContainer
                val loginUseCases = LoginUseCases(
                    homeKitRedContainer.hubAPIRepository,
                    homeKitRedContainer.homeKitRedDatabase.hubRepository(),
                    hubLocalDataService = HubLocalDataService(
                        roomAPIRepository = homeKitRedContainer.roomAPIRepository,
                        roomDatasource = homeKitRedContainer.homeKitRedDatabase.roomRepository(),
                    )
                )
                LoginViewModel(loginUseCases)
            }
        }
    }

}