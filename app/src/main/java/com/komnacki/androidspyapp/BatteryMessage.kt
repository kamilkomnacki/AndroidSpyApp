package com.komnacki.androidspyapp

data class BatteryMessage(
    var percent: String,
    var temperature: Any,
    var voltage: String,
    var technology: String,
    var isCharging: Boolean,
    var chargingSource: String,
    var batteryHealth: String
)
