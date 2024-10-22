package red.tetracube.homekitred.hubcentral.room.create

import androidx.compose.runtime.MutableState
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
import red.tetracube.homekitred.hubcentral.room.create.models.FieldInputEvent
import red.tetracube.homekitred.hubcentral.room.create.models.RoomCreateUIModel
import red.tetracube.homekitred.ui.core.models.UIState

class RoomCreateViewModel(
    private val roomCreateUseCases: RoomCreateUseCases
) : ViewModel() {

    private val _roomCreateState: MutableState<RoomCreateUIModel> = mutableStateOf(RoomCreateUIModel())
    val roomCreateState: State<RoomCreateUIModel>
        get() = _roomCreateState

    private val _uiState = mutableStateOf<UIState>(UIState.Neutral)
    val uiState: State<UIState>
        get() = _uiState

    fun onInputEvent(fieldInputEvent: FieldInputEvent) {
        when (fieldInputEvent) {
            is FieldInputEvent.FieldFocusAcquire -> {
                _roomCreateState.value =
                    when (fieldInputEvent.fieldName) {
                        FieldInputEvent.FieldName.ROOM_NAME -> _roomCreateState.value.copy(
                            roomNameField = _roomCreateState.value.roomNameField.copy(isTouched = true)
                        )
                    }
            }

            is FieldInputEvent.FieldValueInput -> {
                _roomCreateState.value =
                    when (fieldInputEvent.fieldName) {
                        FieldInputEvent.FieldName.ROOM_NAME -> _roomCreateState.value.copy(
                            roomNameField = _roomCreateState.value.roomNameField.copy(
                                value = fieldInputEvent.fieldValue,
                                hasError = _roomCreateState.value.roomNameField.isTouched
                                        && (fieldInputEvent.fieldValue.isBlank()
                                        || !(5..25).contains(fieldInputEvent.fieldValue.length)
                                        || !fieldInputEvent.fieldValue.matches("^[ \\w]+\$".toRegex()))
                            ),
                        )
                    }
                validateForm()
            }
        }
    }

    private fun validateForm() {
        _roomCreateState.value = _roomCreateState.value.copy(
            formIsValid = (!_roomCreateState.value.roomNameField.hasError && _roomCreateState.value.roomNameField.isTouched)
        )
    }

    fun onRoomSubmit() {
        viewModelScope.launch {
            _uiState.value = UIState.Loading
            roomCreateUseCases.createRoom(_roomCreateState.value.roomNameField.value)
            _uiState.value = UIState.FinishedWithSuccess
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val homeKitRedContainer =
                    (this[APPLICATION_KEY] as HomeKitRedApp).homeKitRedContainer
                RoomCreateViewModel(
                    roomCreateUseCases = RoomCreateUseCases(
                        roomAPIRepository = homeKitRedContainer.roomAPIRepository,
                        hubDatasource = homeKitRedContainer.homeKitRedDatabase.hubRepository(),
                        roomDatasource = homeKitRedContainer.homeKitRedDatabase.roomRepository()
                    )
                )
            }
        }
    }
}