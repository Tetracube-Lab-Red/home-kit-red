package red.tetracube.homekitred.ui.hub.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import red.tetracube.homekitred.HomeKitRedApp
import red.tetracube.homekitred.models.errors.HomeKitRedError
import red.tetracube.homekitred.ui.state.UIState
import red.tetracube.homekitred.business.usecases.GlobalDataUseCases
import red.tetracube.homekitred.business.usecases.HubUseCases

class LoginViewModel(
    private val hubUseCases: HubUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState>(UIState.Neutral)
    val uiState: StateFlow<UIState>
        get() = _uiState

    fun onLoginButtonClick(hubAddress: String, hubName: String, hubPassword: String) {
        _uiState.value = UIState.Loading
        viewModelScope.launch {
            val loginResult = hubUseCases.login(hubAddress, hubName, hubPassword)
            if (loginResult.isFailure) {
                _uiState.value = loginResult.exceptionOrNull()
                    ?.let { it as HomeKitRedError }
                    ?.let { UIState.FinishedWithError(it) }
                    ?: UIState.FinishedWithError(HomeKitRedError.GenericError)
            } else {
                _uiState.value = UIState.FinishedWithSuccessContent(Unit)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val homeKitRedContainer =
                    (this[APPLICATION_KEY] as HomeKitRedApp).homeKitRedContainer
                val loginUseCases = HubUseCases(
                    homeKitRedContainer.hubAPIDataSource,
                    homeKitRedContainer.homeKitRedDatabase.hubDataSource(),
                    globalDataUseCases = GlobalDataUseCases(
                        roomDatasource = homeKitRedContainer.homeKitRedDatabase.roomDataSource(),
                        homeKitRedContainer.hubAPIDataSource,
                    )
                )
                LoginViewModel(loginUseCases)
            }
        }
    }

}