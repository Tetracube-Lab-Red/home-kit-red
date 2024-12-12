package red.tetracube.homekitred

import android.app.Application

class HomeKitRedApp : Application() {
    val homeKitRedContainer = HomeKitRedContainer(this)
}