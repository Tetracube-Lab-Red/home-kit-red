package red.tetracube.homekitred.ui.hub.room.create

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
import red.tetracube.homekitred.business.models.errors.HomeKitRedError
import red.tetracube.homekitred.business.models.ui.UIState
import red.tetracube.homekitred.business.usecases.RoomUseCases

class RoomCreateViewModel(
    private val roomUseCases: RoomUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState>(UIState.Neutral)
    val uiState: StateFlow<UIState>
        get() = _uiState

    fun onRoomSubmit(roomName: String) {
        viewModelScope.launch {
            _uiState.value = UIState.Loading
            val roomCreateResult = roomUseCases.createRoom(roomName)
            _uiState.value = if (roomCreateResult.isSuccess) {
                UIState.FinishedWithSuccessContent(Unit)
            } else {
                roomCreateResult.exceptionOrNull()
                    ?.let { it as HomeKitRedError }
                    ?.let { UIState.FinishedWithError(it) }
                    ?: UIState.FinishedWithError(HomeKitRedError.GenericError)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val homeKitRedContainer =
                    (this[APPLICATION_KEY] as HomeKitRedApp).homeKitRedContainer
                RoomCreateViewModel(
                    roomUseCases = RoomUseCases(
                        hubDataSource = homeKitRedContainer.homeKitRedDatabase.hubDataSource(),
                        hubApiDataSource = homeKitRedContainer.hubAPIDataSource,
                        roomDataSource = homeKitRedContainer.homeKitRedDatabase.roomDataSource()
                    )
                )
            }
        }
    }
}