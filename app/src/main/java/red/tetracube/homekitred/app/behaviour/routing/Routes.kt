package red.tetracube.homekitred.app.behaviour.routing

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes {

    @Serializable
    data object Splash : Routes()

}