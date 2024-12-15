package red.tetracube.homekitred.ui.state

import red.tetracube.homekitred.models.errors.HomeKitRedError

sealed class UIState {
    
    data object Neutral : UIState()
    data object Loading : UIState()
    data class FinishedWithSuccessContent<T>(val content: T): UIState()
    data class FinishedWithError<E : HomeKitRedError>(val error: E) : UIState()
    
}