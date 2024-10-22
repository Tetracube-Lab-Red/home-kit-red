package red.tetracube.homekitred.ui.core.models

import red.tetracube.homekitred.domain.HomeKitRedError

sealed class UIState {
    
    data object Neutral : UIState()
    data object Loading : UIState()
    data object FinishedWithSuccess : UIState()
    data class FinishedWithSuccessContent<T>(val content: T): UIState()
    data class FinishedWithError<E : HomeKitRedError>(val error: E) : UIState()
    
}