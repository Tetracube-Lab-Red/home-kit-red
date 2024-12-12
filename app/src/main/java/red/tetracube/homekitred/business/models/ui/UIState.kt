package red.tetracube.homekitred.business.models.ui

import red.tetracube.homekitred.business.models.errors.HomeKitRedError

sealed class UIState {
    
    data object Neutral : UIState()
    data object Loading : UIState()
    data class FinishedWithSuccessContent<T>(val content: T): UIState()
    data class FinishedWithError<E : HomeKitRedError>(val error: E) : UIState()
    
}