package red.tetracube.homekitred

import android.app.Application
import red.tetracube.homekitred.app.container.HomeKitRedContainer

class HomeKitRedApp : Application() {
    val homeKitRedContainer = HomeKitRedContainer()
}