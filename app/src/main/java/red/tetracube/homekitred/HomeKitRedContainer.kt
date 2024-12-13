package red.tetracube.homekitred

import android.content.Context
import red.tetracube.homekitred.data.api.datasource.HubAPIDataSource
import red.tetracube.homekitred.data.api.datasource.IoTAPIDataSource
import red.tetracube.homekitred.data.db.HomeKitRedDatabase

class HomeKitRedContainer(context: Context) {

    val hubAPIDataSource by lazy { HubAPIDataSource() }
    val ioTAPIDataSource by lazy { IoTAPIDataSource() }

    val homeKitRedDatabase by lazy { HomeKitRedDatabase.getInstance(context) }

}