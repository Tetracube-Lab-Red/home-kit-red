package red.tetracube.homekitred.app.models

import red.tetracube.homekitred.app.exceptions.HomeKitRedError

sealed class UIState {
    
    data object Neutral : UIState()
    data object Loading : UIState()
    data object FinishedWithSuccess : UIState()
    data class FinishedWithSuccessContent<T>(val content: T): UIState()
    data class FinishedWithError<E : HomeKitRedError>(val error: E) : UIState()
    
}