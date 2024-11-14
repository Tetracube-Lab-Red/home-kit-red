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
import red.tetracube.homekitred.app.exceptions.HomeKitRedError
import red.tetracube.homekitred.hubcentral.room.create.models.FieldInputEvent
import red.tetracube.homekitred.hubcentral.room.create.models.RoomCreateUIModel
import red.tetracube.homekitred.app.models.UIState

class RoomCreateViewModel(
    private val roomCreateUseCases: RoomCreateUseCases
) : ViewModel() {

    private val formValidationUseCases = FormValidationUseCases()

    private val _roomCreateState: MutableState<RoomCreateUIModel> =
        mutableStateOf(RoomCreateUIModel())
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
                                isDirty = true
                            ),
                        )
                    }
                validateForm()
            }
        }
    }

    private fun validateForm() {
        if (_roomCreateState.value.roomNameField.isDirty) {
            val validationPair =
                formValidationUseCases.roomNameHasError(_roomCreateState.value.roomNameField.value)
            _roomCreateState.value = roomCreateState.value.copy(
                roomNameField = _roomCreateState.value.roomNameField.copy(
                    isValid = validationPair.component1(),
                    supportingText = validationPair.component2()
                )
            )
        }

        _roomCreateState.value = _roomCreateState.value.copy(
            formIsValid = (_roomCreateState.value.roomNameField.isDirty && _roomCreateState.value.roomNameField.isValid)
        )
    }

    fun onRoomSubmit() {
        viewModelScope.launch {
            _uiState.value = UIState.Loading
            val roomCreateResult =
                roomCreateUseCases.createRoom(_roomCreateState.value.roomNameField.value)
            _uiState.value = if (roomCreateResult.isSuccess) {
                UIState.FinishedWithSuccess
            } else {
                UIState.FinishedWithError(
                    roomCreateResult.exceptionOrNull()?.let { it as HomeKitRedError }!!
                )
            }
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