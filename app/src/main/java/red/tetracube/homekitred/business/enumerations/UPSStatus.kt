package red.tetracube.homekitred.business.enumerations

enum class UPSStatus {
    ONLINE,
    ON_BATTERY,
    LOW_BATTERY,
    HIGH_BATTERY,
    REPLACE_BATTERY,
    BYPASS,
    NULL,
    CHARGING,
    DISCHARGING,
    CALIBRATING,
    OFFLINE,
    OVERLOADED,
    IN_TRIMMING,
    IN_BOOSTING,
    FORCED_SHUTDOWN
}